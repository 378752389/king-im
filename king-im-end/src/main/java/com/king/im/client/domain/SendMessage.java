package com.king.im.client.domain;

import com.king.im.common.enums.MessageTypeEnum;
import com.king.im.client.domain.message.BaseMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.client.domain.type.SenderInfo;
import lombok.Data;

/**
 * 后端控制消息转发业务模型
 * @param
 */
@Data
public class SendMessage {

    /**
     * 唯一的消息id
     */
    private Long msgId;


    /**
     * 1-单聊 2-群聊
     */
    private Integer sendType;

    /**
     * 发送者用户信息
     */
    private SenderInfo senderInfo;

    /**
     * 接受者用户信息
     */
    private ReceiverInfo receiverInfo;

    /**
     * 发送给自己其他终端
     */
    private Boolean sendToSelf = true;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 消息内容
     */
    private BaseMessage message;

    /**
     * 消息类型
     */
    private MessageTypeEnum messageType;
}
