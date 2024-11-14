package com.king.im.msg.convert;

import cn.hutool.core.collection.CollUtil;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.common.domain.RecMessage;
import com.king.im.common.enums.MessageStatusEnum;
import com.king.im.common.domain.type.IMUserInfo;

import cn.hutool.core.util.StrUtil;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.msg.domain.MsgReq;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.msg.domain.extra.Extra;
import com.king.im.client.domain.SendMessage;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.common.enums.MessageTypeEnum;
import com.king.im.client.domain.message.BaseMessage;
import com.king.im.client.domain.type.SenderInfo;
import com.king.im.server.domain.ReceiveMessage;
import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.data.ChatData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MsgConvert {

    /**
     * 消息请求转换为 消息实体
     *
     * @param req
     * @return
     */
    public static Msg buildMsg(MsgReq req) {
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        Extra extra = req.getExtra();
        List<Long> atUids = req.getAtUids();
        String ats = StrUtil.join(",", atUids);

        return Msg.builder()
                .content(req.getText())
                .type(req.getMsgType())
                .roomId(req.getRoomId())
                .fromUid(userInfo.getUid())
                .extra(extra)
                .atUids(ats)
                .status(MessageStatusEnum.WAITING_SEND.getType())
                .referMsgId(req.getReferMsgId())
                .sendTerminal(userInfo.getTerminal())
                .toUid(req.getToUid())
                .createTime(new Date())
                .build();
    }

    /**
     * 构建 client 发送消息领域模型
     *
     * @param msg
     * @return
     */
    public static SendMessage buildSendMessage(Msg msg) {
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.valueOf(msg.getType());
        SendMessage sendMessage = new SendMessage();


        sendMessage.setMsgId(msg.getId());

        // todo 使用时需要自行构建消息接收者信息
        if (msg.getRoomId() == null) {
            sendMessage.setSendType(ChatTypeEnum.SINGLE.getType());
        } else {
            sendMessage.setSendType(ChatTypeEnum.GROUP.getType());
        }
        sendMessage.setSenderInfo(new SenderInfo(msg.getFromUid(), msg.getSendTerminal()));
        sendMessage.setSendToSelf(true);
        sendMessage.setStatus(msg.getStatus());

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setId(msg.getId());
        baseMessage.setSendTime(msg.getCreateTime());
        baseMessage.setSenderId(msg.getFromUid());
        baseMessage.setChatId(msg.getRoomId() == null ? msg.getToUid() : msg.getRoomId());
        baseMessage.setReferMsgId(msg.getReferMsgId());

        if (StrUtil.isEmpty(msg.getAtUids())) {
            baseMessage.setAtUids(new ArrayList<>());
        } else {
            List<Long> atUids = StrUtil.split(msg.getAtUids(), ",").stream().map(Long::parseLong).collect(Collectors.toList());
            baseMessage.setAtUids(atUids);
        }
        baseMessage.setText(msg.getContent());
        baseMessage.setExtra(msg.getExtra());

        sendMessage.setMessage(baseMessage);
        sendMessage.setMessageType(messageTypeEnum);

        return sendMessage;
    }

    /**
     * 构建 client 发送消息领域模型
     *
     * @param chatData
     * @return
     */
    public static SendMessage buildSendMessage(ChatData chatData) {
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.valueOf(chatData.getContentType());
        SendMessage sendMessage = new SendMessage();


        sendMessage.setMsgId(chatData.getId());

        // todo 使用时需要自行构建消息接收者信息
        if (chatData.getRoomId() == null) {
            sendMessage.setSendType(ChatTypeEnum.SINGLE.getType());
        } else {
            sendMessage.setSendType(ChatTypeEnum.GROUP.getType());
        }
        sendMessage.setSenderInfo(new SenderInfo(chatData.getFromUid(), chatData.getSendTerminal()));
        sendMessage.setSendToSelf(true);
        sendMessage.setStatus(chatData.getStatus());

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setId(chatData.getId());
        baseMessage.setSendTime(new Date(chatData.getSendTime()));
        baseMessage.setSenderId(chatData.getFromUid());
        baseMessage.setChatId(chatData.getRoomId() == null ? chatData.getToUid() : chatData.getRoomId());
        baseMessage.setReferMsgId(chatData.getReferMsgId());
        baseMessage.setReferMsg(chatData.getReferMsg());

        if (CollUtil.isEmpty(chatData.getAtUids())) {
            baseMessage.setAtUids(new ArrayList<>());
        } else {
            baseMessage.setAtUids(chatData.getAtUids());
        }
        baseMessage.setText((String) chatData.getContent());
        baseMessage.setExtra(chatData.getExtra());

        sendMessage.setMessage(baseMessage);
        sendMessage.setMessageType(messageTypeEnum);

        return sendMessage;
    }

    /**
     * 通过消息实体 构建 消息数据
     *
     * @param msg
     * @return
     */
    public static ChatData buildChatData(Msg msg) {
        ChatData chatData = new ChatData();

        chatData.setId(msg.getId());
        chatData.setRoomId(msg.getRoomId());
        chatData.setFromUid(msg.getFromUid());
        chatData.setToUid(msg.getToUid());
        chatData.setReferMsgId(msg.getReferMsgId());

        if (StrUtil.isEmpty(msg.getAtUids())) {
            chatData.setAtUids(new ArrayList<>());
        } else {
            List<Long> atUids = StrUtil.split(msg.getAtUids(), ",").stream().map(Long::parseLong).collect(Collectors.toList());
            chatData.setAtUids(atUids);
        }
        chatData.setExtra(msg.getExtra());
        chatData.setContent(msg.getContent());
        chatData.setContentType(msg.getType());
        chatData.setSendTime(msg.getCreateTime().getTime());
        chatData.setType(chatData.getRoomId() == null ? 1 : 2);
        chatData.setStatus(msg.getStatus());

        return chatData;
    }

    /**
     * server 消息发送模型 构建服务端协议命令
     *
     * @param receiveMessage
     * @return
     */
    public static CMD buildIMChatCMD(RecMessage receiveMessage) {
        CMD CMD = new CMD();
        ChatData chatData = new ChatData();

        chatData.setId(receiveMessage.getMsgId());
        // 单聊：接收方uid； 群聊：房间号
        chatData.setFromUid(receiveMessage.getSender().getId());
        /**
         * 文件消息为消息内容，其他媒体消息为媒体名称
         */
        chatData.setContent(receiveMessage.getContent());
        chatData.setExtra(receiveMessage.getExtra());
        chatData.setAtUids(receiveMessage.getAtUids());
        chatData.setReferMsgId(receiveMessage.getReferMsgId());
        chatData.setReferMsg(receiveMessage.getReferMsg());
        chatData.setContentType(receiveMessage.getType());
        chatData.setSendTime(receiveMessage.getSendTime().getTime());
        chatData.setType(receiveMessage.getChatType());
        chatData.setStatus(receiveMessage.getStatus());

        // 消息接收方id
        Long chatId = receiveMessage.getChatId();
        if (ChatTypeEnum.SINGLE.getType().equals(receiveMessage.getChatType())) {
            chatData.setToUid(chatId);
        } else if (ChatTypeEnum.GROUP.getType().equals(receiveMessage.getChatType())) {
            chatData.setRoomId(chatId);
        }
        // 命令封装
        CMD.setCmd(CMDType.CHAT);
        CMD.setData(chatData);

        return CMD;
    }
}
