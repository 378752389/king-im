package com.king.im.user.domain.enums;

import lombok.Getter;

@Getter
public enum RegisterTypeEnum {
    EMAIL(1, "电子邮箱"),
    SIMPLE(9, "用户名密码-简单部署");

    private final Integer type;

    private final String desc;

    RegisterTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
