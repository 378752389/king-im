package com.king.im.sender.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum TerminalTypeEnum {

    PC(1, "电脑网站"),

    WEB(2, "手机网站"),

    APP(3, "APP");

    private final Integer type;

    private final String desc;

    TerminalTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static List<Integer> all() {
        return Arrays.stream(TerminalTypeEnum.values()).map(TerminalTypeEnum::getType).collect(Collectors.toList());
    }
}
