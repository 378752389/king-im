package com.king.im.client;

import com.king.im.client.domain.SendMessage;
import com.king.im.client.domain.message.BaseMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.client.domain.type.SenderInfo;
import com.king.im.common.enums.MessageStatusEnum;
import com.king.im.common.enums.MessageTypeEnum;

import java.util.Date;

public class SendMessageFactory {

    /**
     *
     * @param chatId  会话标识
     * @param chatType 会话类型
     * @param senderInfo 发送者信息
     * @param receiverInfo 接受者信息
     * @param notice 通知内容
     * @return
     */
    public static SendMessage buildNoticeMessage(Long chatId, Integer chatType, SenderInfo senderInfo, ReceiverInfo receiverInfo, String notice) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setMsgId(0L);
        sendMessage.setSendType(chatType);
        sendMessage.setSenderInfo(senderInfo);
        sendMessage.setReceiverInfo(receiverInfo);
        sendMessage.setSendToSelf(true);
        sendMessage.setStatus(MessageStatusEnum.WAITING_SEND.getType());

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setId(0L);
        baseMessage.setSendTime(new Date());
        baseMessage.setSenderId(senderInfo.getUid());
        baseMessage.setChatId(chatId);
        baseMessage.setReferMsgId(null);
        baseMessage.setAtUids(null);
        baseMessage.setText(notice);
        baseMessage.setExtra(null);

        sendMessage.setMessage(baseMessage);
        sendMessage.setMessageType(MessageTypeEnum.NOTICE);

        return sendMessage;
    }
}
