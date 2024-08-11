package com.king.im.sender.domain.enums;

import lombok.Getter;

@Getter
public enum ChatTypeEnum {

    SINGLE(1, "单聊"),

    GROUP(2, "群聊");

    private final Integer type;

    private final String desc;

    ChatTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
