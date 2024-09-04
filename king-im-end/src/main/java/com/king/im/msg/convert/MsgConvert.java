package com.king.im.msg.convert;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.common.enums.MessageStatusEnum;
import com.king.im.server.domain.type.IMUserInfo;
import com.google.common.collect.Lists;

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
        baseMessage.setAtUids(StrUtil.split(msg.getAtUids(), ",").stream().map(Long::valueOf).collect(Collectors.toList()));
        baseMessage.setText(msg.getContent());
        baseMessage.setExtra(msg.getExtra());

        sendMessage.setMessage(baseMessage);
        sendMessage.setMessageType(messageTypeEnum);

        return sendMessage;
    }

    public static ChatData buildChatData(Msg msg) {
        ChatData chatData = new ChatData();

        chatData.setId(msg.getId());
        chatData.setRoomId(msg.getRoomId());
        chatData.setFromUid(msg.getFromUid());
        chatData.setToUid(msg.getToUid());
        chatData.setReferMsgId(msg.getReferMsgId());
        List<Long> atUids = StrUtil.split(msg.getAtUids(), ",").stream().map(Long::valueOf).collect(Collectors.toList());
        chatData.setAtUids(atUids);
        chatData.setExtra(msg.getExtra());
        chatData.setContent(msg.getContent());
        chatData.setContentType(msg.getType());
        chatData.setSendTime(msg.getCreateTime().getTime());
        chatData.setType(chatData.getRoomId() == null ? 1 : 2);
        chatData.setStatus(msg.getStatus());

        return chatData;
    }

//    public static CMD buildIMCMD(SendMessage sendMessage) {
//        CMD CMD = new CMD();
//        ChatData chatData = new ChatData();
//
//        BaseMessage message = sendMessage.getMessage();
//
//        // 单聊：接收方uid； 群聊：房间号
//        chatData.setId(message.getId());
//        chatData.setFromUid(sendMessage.getSenderInfo().getUid());
//        /**
//         * 文件消息为消息内容，其他媒体消息为媒体名称
//         */
//        chatData.setContent(message.getText());
//        chatData.setExtra(message.getExtra());
//        chatData.setAtUids(message.getAtUids());
//        chatData.setReferMsgId(message.getReferMsgId());
//        chatData.setContentType(sendMessage.getMessageType().getType());
//        chatData.setSendTime(message.getSendTime().getTime());
//        chatData.setType(sendMessage.getSendType());
//        chatData.setStatus(sendMessage.getStatus());
//
//        // 消息接收方id
//        Long chatId = message.getChatId();
//        if (ChatTypeEnum.SINGLE.getType().equals(sendMessage.getSendType())) {
//            chatData.setToUid(chatId);
//        } else if (ChatTypeEnum.GROUP.getType().equals(sendMessage.getSendType())) {
//            chatData.setRoomId(chatId);
//        }
//        // 命令封装
//        CMD.setCmd(CMDType.CHAT);
//        CMD.setData(chatData);
//
//        return CMD;
//    }

    public static ReceiveMessage buildReceiveMessage(SendMessage sendMessage) {
        ReceiveMessage receiveMessage = new ReceiveMessage();

        SenderInfo senderInfo = sendMessage.getSenderInfo();
        IMUserInfo sender = new IMUserInfo(senderInfo.getUid(), senderInfo.getTerminal());
        receiveMessage.setSender(sender);

        ReceiverInfo receiverInfo = sendMessage.getReceiverInfo();
        List<Long> receiverIds = receiverInfo.getReceiverIds();
        List<Integer> receiveTerminalTypes = receiverInfo.getReceiveTerminalTypes();

        List<IMUserInfo> receivers = new ArrayList<>();

        for (Long receiverId : receiverIds) {
            for (Integer receiveTerminalType : receiveTerminalTypes) {
                IMUserInfo receiver = new IMUserInfo(receiverId, receiveTerminalType);
                receivers.add(receiver);
            }
        }

        receiveMessage.setReceivers(receivers);
        BaseMessage baseMessage = sendMessage.getMessage();

        receiveMessage.setTargetId(baseMessage.getChatId());
        receiveMessage.setChatType(sendMessage.getSendType());
        receiveMessage.setMsgId(sendMessage.getMsgId());
        receiveMessage.setStatus(sendMessage.getStatus());
        receiveMessage.setSendTime(baseMessage.getSendTime());
        receiveMessage.setReferMsgId(baseMessage.getReferMsgId());
        receiveMessage.setAtUids(baseMessage.getAtUids());
        receiveMessage.setContent(baseMessage.getText());
        receiveMessage.setExtra(baseMessage.getExtra());
        receiveMessage.setType(sendMessage.getMessageType().getType());

        return receiveMessage;
    }

    public static CMD buildIMCMD(ReceiveMessage receiveMessage) {
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
        chatData.setContentType(receiveMessage.getType());
        chatData.setSendTime(receiveMessage.getSendTime().getTime());
        chatData.setType(receiveMessage.getChatType());
        chatData.setStatus(receiveMessage.getStatus());

        // 消息接收方id
        Long chatId = receiveMessage.getTargetId();
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
