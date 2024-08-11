package com.king.im.sender.domain.enums;

import lombok.Getter;

@Getter
public enum MessageStatusEnum {

    WAITING_SEND(1, "待发送"),

    SEND(2, "已发送"),

    HAS_READ(3, "消息以读"),

    REVOKE(4, "消息撤回");

    private final Integer type;

    private final String desc;

    MessageStatusEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
