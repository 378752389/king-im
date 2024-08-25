package com.king.im.server.domain;

import com.king.im.msg.domain.extra.Extra;
import com.king.im.server.domain.type.IMUserInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReceiveMessage {

    /**
     * 消息发送者信息
     */
    private IMUserInfo sender;

    /**
     * 消息接受者信息
     */
    private List<IMUserInfo> receivers;

    /**
     * chatType = 1 单聊， 消息接受者id； chatType = 2 群聊， 房间id；
     */
    private Long targetId;

    /**
     * 消息类型 1-单聊； 2-群聊
     */
    private Integer chatType;
    /**
     * 消息id
     */
    private Long msgId;

    /**
     * 消息状态
     */
    private Integer status;

    /**
     * 消息发送时间
     */
    private Date sendTime;

    /**
     * 回执id
     */
    private Long referMsgId;

    /**
     * 群聊 at 用户列表
     */
    private List<Long> atUids;

    /**
     * 消息文本内容
     */
    private String content;

    /**
     * 消息附加信息
     */
    private Extra extra;

    /**
     * 消息类型 1-文本
     */
    private Integer type;
}
