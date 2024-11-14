package com.king.im.client.domain.message;

import com.king.im.msg.domain.extra.Extra;
import com.king.im.common.domain.type.ReferMsg;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BaseMessage {

    private Long id;

    /**
     * 消息发送时间
     */
    private Date sendTime;

    private Long senderId;

    /**
     * 消息接收方id， 房间号或者peerId
     */
    private Long chatId;

    private Long referMsgId;

    private ReferMsg referMsg;

    private List<Long> atUids;

    private String text;

    private Extra extra;
}
