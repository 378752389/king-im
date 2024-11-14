package com.king.im.common.domain;

import com.king.im.common.domain.type.IMUserInfo;
import com.king.im.common.domain.type.ReferMsg;
import com.king.im.msg.domain.extra.Extra;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接收消息领域模型
 */
@Data
public class RecMessage {

    /**
     * 发送方设备信息（带有终端标识）
     */
    IMUserInfo sender;

    /**
     * 接收方设备信息（带有终端标识）
     */
    IMUserInfo receiver;

    /**
     * 目标id; chatType = 1 => 接收方用户id； chatType = 2 => 群标识；
     */
    private Long chatId;

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
     * 引用消息id
     */
    private Long referMsgId;

    /**
     * 引用消息
     */
    private ReferMsg referMsg;

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
