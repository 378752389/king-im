package com.king.im.msg.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.cursor.CursorResult;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.listener.event.MsgEvent;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.msg.mapper.MsgMapper;
import com.king.im.msg.service.MessageService;
import com.king.im.sender.IMSender;
import com.king.im.sender.domain.SendMessage;
import com.king.im.sender.domain.enums.MessageStatusEnum;
import com.king.im.sender.domain.type.ReceiverInfo;
import com.king.im.sender.protocol.data.ChatData;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.mapper.RoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private ApplicationEventPublisher publisher;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private IMSender imSender;

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
                msg.setStatus(2);
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
    public List<ChatData> loadHistoryMessage(MsgReq req) {

        return null;
    }

    @Override
    public void loadOfflineMessage(Long minMsgId, Long userId) {

        List<Msg> offlineMsg = new LinkedList<>();
        List<Msg> offlineUserMsg = getSingleChatOfflineMessage(minMsgId, userId);
        offlineMsg.addAll(offlineUserMsg);
        log.info("userId: {}, 消息偏移量：{}， 拉取朋友离线消息数量：{}", userId, minMsgId, offlineMsg.size());

        List<RoomDo> roomList = roomMapper.getRoomList(userId);
        List<Long> roomIds = roomList.stream().map(RoomDo::getRoomId).collect(Collectors.toList());
        List<Msg> offlineGroupMsg = getGroupChatOfflineMessage(minMsgId, roomIds);
        offlineMsg.addAll(offlineGroupMsg);

        log.info("userId: {}, 消息偏移量：{}， 拉取总离线消息数量：{}", userId, minMsgId, offlineMsg.size());
        for (Msg msg : offlineMsg) {
            SendMessage sendMessage = MsgConvert.buildSendMessage(msg);
            sendMessage.setReceiverInfo(new ReceiverInfo(userId));
            imSender.send(sendMessage);
        }
    }

    public List<Msg> getSingleChatOfflineMessage(Long minMsgId, Long userId) {
        return msgMapper.getOfflineMsgByUserId(minMsgId, userId);
    }

    public List<Msg> getGroupChatOfflineMessage(Long minMsgId, List<Long> roomIds) {
        List<Msg> offlineMessageList = new LinkedList<>();
        for (Long roomId : roomIds) {
            List<Msg> msgList = msgMapper.getOfflineMsgByRoomId(minMsgId, roomId);
            offlineMessageList.addAll(msgList);
        }
        return offlineMessageList;
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
            msg.setType(MessageStatusEnum.SEND.getType());
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
