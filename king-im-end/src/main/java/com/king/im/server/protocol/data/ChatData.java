package com.king.im.server.protocol.data;

import com.king.im.common.domain.type.ReferMsg;
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

    private ReferMsg referMsg;

    private Integer sendTerminal;

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
     * 消息类型， 1 - 单聊，2 - 群聊  3 - 加载
     */
    private Integer type;

    /**
     * 消息状态, 1-正常 2-撤回 3-删除 9-失败
     */
    private Integer status;
}
