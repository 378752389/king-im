package com.king.im.sender.protocol.data;

import com.king.im.msg.domain.extra.Extra;
import lombok.Data;

import java.util.List;

@Data
public class ChatData {

    private Long id;

    private Long roomId;

    private Long fromUid;

    private Long toUid;

    private Long referMsgId;

    private List<Long> atUids;

    private Extra extra;

    /**
     * 消息内容
     */
    private Object content;

    /**
     * 内容类型
     */
    private Integer contentType;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 消息类型， 单聊，群聊
     */
    private Integer type;

    /**
     * 消息状态, 1-正常 2-撤回 3-删除 9-失败
     */
    private Integer status;
}
