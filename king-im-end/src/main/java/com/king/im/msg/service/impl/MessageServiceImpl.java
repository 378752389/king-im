package com.king.im.msg.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.king.im.common.cursor.CursorResult;
import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.common.redisson.DistributedLock;
import com.king.im.listener.event.MsgEvent;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.msg.mapper.MsgMapper;
import com.king.im.msg.service.MessageCache;
import com.king.im.msg.service.MessageService;
import com.king.im.client.IMSender;
import com.king.im.client.domain.SendMessage;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.common.enums.MessageStatusEnum;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.server.protocol.data.ChatData;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.mapper.FriendMapper;
import com.king.im.social.mapper.RoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private ApplicationEventPublisher publisher;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private IMSender imSender;
    @Resource
    private MessageCache messageCache;

    @Override
    public Long sendMsg(MsgReq req) {
        Msg msg = MsgConvert.buildMsg(req);
        msgMapper.insert(msg);
        log.info("收到消息: {}", msg);
        publisher.publishEvent(new MsgEvent(this, msg));
        return msg.getId();
    }

    @Override
    public ChatData getMsg(Long id) {
        Msg msg = msgMapper.selectById(id);
        return MsgConvert.buildChatData(msg);
    }

    @Override
    @Transactional
    public Long readedMsg(Long chatId, Integer type) {
        Long uid = RequestInfoHolder.getUid();

        long readedCount = 0;
        if (type == 1) {
            // 查询所有的单聊
            List<Msg> unreadMsg = msgMapper.getUnreadMsg(chatId, uid, type);
            unreadMsg.forEach(msg -> {
                msg.setStatus(MessageStatusEnum.SEND.getType());
                msgMapper.updateById(msg);
            });
            readedCount = unreadMsg.size();
        } else if (type == 2) {
            // todo
        }
        //
        return readedCount;
    }

    @Override
    public void revokeMsg(Long msgId) {
        Long uid = RequestInfoHolder.getUid();
        Msg msg = msgMapper.selectById(msgId);
        if (msg == null) {
            throw new GlobalException("消息不存在");
        }

        if (!uid.equals(msg.getFromUid())) {
           throw new GlobalException("不能撤销非自身发送消息");
        }

        Date createTime = msg.getCreateTime();
        if (DateUtil.between(createTime, new Date(), DateUnit.MINUTE) > 3) {
            throw new GlobalException("消息发送超过3分钟，不能撤回");
        }

        if (msg.getStatus().equals(MessageStatusEnum.REVOKE.getType())) {
            throw new GlobalException("消息已经撤回");
        }

        msg.setStatus(MessageStatusEnum.REVOKE.getType());
        msgMapper.updateById(msg);

        log.info("撤回消息, {}", msg);
        publisher.publishEvent(new MsgEvent(this, msg));
    }

    @Override
    public CursorResult<ChatData> getHistoryMessageCursorPage(Long chatId, Integer chatType, Long cursor, Long size) {
        List<Msg> msgList = new ArrayList<>();
        Long uid = RequestInfoHolder.getUid();

        if (ChatTypeEnum.GROUP.getType().equals(chatType)) {
            if (!roomMapper.isUserExist(chatId, uid)) {
                log.error("userId: {}, roomId: {}; 你未加入群聊", uid, chatId);
                throw new GlobalException("用户未加入群聊");
            }
            msgList = msgMapper.getRoomMsgCursorPage(chatId, cursor, size);
        }

        if (ChatTypeEnum.SINGLE.getType().equals(chatType)) {
            if (!friendMapper.isFriend(uid, chatId)) {
                log.error("userId: {}, peerId: {}; 和对方非好友关系", uid, chatId);
                throw new GlobalException("和对方非好友关系");
            }
            msgList = msgMapper.getPeerMsgCursorPage(uid, chatId, cursor, size);
        }

        List<ChatData> chatDataList = msgList.stream().map(MsgConvert::buildChatData).collect(Collectors.toList());

        long newCur = 0L;
        if (chatDataList.size() > 0) {
            newCur = msgList.get(msgList.size() - 1).getId() - 1;
        }

        CursorResult<ChatData> result = CursorResult.<ChatData>builder()
                .cursor(newCur)
                .data(chatDataList)
                .isLast(msgList.size() != size)
                .size(msgList.size())
                .build();

        return result;
    }

    // todo 事务或分布式锁优先级
    @Override
    @Async
    @DistributedLock(lockKey = "'loadMessage:'+#userId", waitTime = 0)
    @Transactional
    public void pullMessage(Long minMsgId, Long userId) {
        List<Msg> result = new ArrayList<>();

        // 拉取1000条私聊消息
        List<Msg> singleMsgList = msgMapper.getSingleMsgList(minMsgId, userId);
        if (CollUtil.isNotEmpty(singleMsgList)) {
            result.addAll(singleMsgList);
        }

        int singleSize = result.size();

        // 拉群各个群聊最近100条群聊消息
        List<RoomDo> roomList = roomMapper.getRoomList(userId);
        roomList.forEach(roomDo -> {
            List<Msg> roomMsgList = msgMapper.getRoomMsgList(minMsgId, roomDo.getRoomId());

            // 修改群聊消息为状态2 （群聊消息没有己发送状态）
            for (Msg msg : roomMsgList) {
                msg.setStatus(MessageStatusEnum.SEND.getType());
            }

            if (CollUtil.isNotEmpty(roomMsgList)) {
                result.addAll(roomMsgList);
            }
        });

        int roomSize = result.size() - singleSize;

        log.info("拉取最近聊天消息；userId: {}, 消息偏移: {}, 私聊消息数量：{}, 群聊消息数量：{}", userId, minMsgId, singleSize, roomSize);

        for (Msg msg : result) {
            SendMessage sendMessage = MsgConvert.buildSendMessage(msg);
            sendMessage.setReceiverInfo(new ReceiverInfo(userId));
            imSender.send(sendMessage);
        }
    }


    @Override
    @Transactional
    @Async
    @DistributedLock(lockKey = "'loadOfflineMessage:'+#userId", waitTime = 0)
    public void loadOfflineMessage(Long userId) {

        List<Msg> offlineMsg = new LinkedList<>();
        List<Msg> offlineSingleMsgList = msgMapper.getOfflineSingleMsgList(userId);
        if (CollUtil.isNotEmpty(offlineSingleMsgList)) {
            offlineMsg.addAll(offlineSingleMsgList);
        }
        int singleSize = offlineMsg.size();

        // 批量更新离线消息状态为已读
        for (Msg msg : offlineSingleMsgList) {
            Msg update = new Msg();
            update.setId(msg.getId());
            update.setStatus(MessageStatusEnum.SEND.getType());
            msgMapper.updateById(update);
        }

        List<RoomDo> roomList = roomMapper.getRoomList(userId);
        roomList.forEach(roomDo -> {
            Long position = messageCache.getRoomMessagePosition(roomDo.getRoomId(), userId);
            if (position == null) {
                position = 0L;
            }
            List<Msg> offlineRoomMsgList = msgMapper.getRoomMsgList(position, roomDo.getRoomId());
            if (CollUtil.isNotEmpty(offlineRoomMsgList)) {
                offlineMsg.addAll(offlineRoomMsgList);

                Long newPosition = offlineRoomMsgList.get(0).getId();
                messageCache.setRoomMessagePosition(roomDo.getRoomId(), userId, newPosition);
            }

        });
        int roomSize = offlineMsg.size() - singleSize;
        log.info("拉取总离线聊天消息；userId: {}, 私聊离线消息数量：{}, 群聊离线消息数量: {};", userId, singleSize, roomSize);

        for (Msg msg : offlineMsg) {
            SendMessage sendMessage = MsgConvert.buildSendMessage(msg);
            sendMessage.setReceiverInfo(new ReceiverInfo(userId));
            imSender.send(sendMessage);
        }
    }

    @Override
    public CursorResult<ChatData> getMsgCursorPage(MsgCursorReq req) {
        List<Msg> page = null;
        CursorResult<ChatData> result = new CursorResult<>();
        if (req.getRoomId() != null) {
            page = msgMapper.getRoomMsgCursorPage(req.getRoomId(), req.getCursor(), req.getSize());
        } else {
            // 不存在，返回空列表
            if (req.getUid() == null || req.getPeerUid() == null) {
                return result;
            }
            page = msgMapper.getPeerMsgCursorPage(req.getUid(), req.getPeerUid(), req.getCursor(), req.getSize());
        }
//        List<MsgDTO> msgDTOs = page.stream().map(MsgConvert::buildMsgDTO).collect(Collectors.toList());

        return null;
    }

    @Override
    public int updateMsgToSendStatus(Long msgId) {
        Msg msg = msgMapper.selectById(msgId);
        if (msg == null) {
            return 0;
        }
        if (MessageStatusEnum.WAITING_SEND.getType().equals(msg.getStatus())) {
            msg.setStatus(MessageStatusEnum.SEND.getType());
            return msgMapper.updateById(msg);
        }
        return 0;
    }

    @Override
    public int updateMsgToHasReadStatus(Long msgId) {
        Msg msg = msgMapper.selectById(msgId);
        if (msg == null) {
            return 0;
        }
        if (MessageStatusEnum.SEND.getType().equals(msg.getStatus())) {
            msg.setType(MessageStatusEnum.HAS_READ.getType());
            return msgMapper.updateById(msg);
        }
        return 0;
    }

    @Override
    public int updateMsgToRevokeStatus(Long msgId) {
        Msg msg = msgMapper.selectById(msgId);
        if (msg == null) {
            return 0;
        }

        // todo

        msg.setType(MessageStatusEnum.HAS_READ.getType());
        return msgMapper.updateById(msg);
    }
}
