package com.king.im.server.domain;

import com.king.im.server.domain.type.IMUserInfo;
import lombok.Data;

import java.util.List;

@Data
public class ReceiverMessage {

    /**
     * 消息命令类型
     */
    private Integer cmd;

    /**
     * 消息发送者信息
     */
    private IMUserInfo sender;

    /**
     * 消息接受者信息
     */
    private List<IMUserInfo> receivers;


    private Object data;
}
