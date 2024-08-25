package com.king.im.common.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MessageTypeEnum {

    TEXT(1, "文本消息"),
    PICTURE(2, "图片消息"),
    Audio(3, "音频消息"),
    Video(4, "视频消息"),
    File(5, "文件消息");
//    VOICE_CALL(6, "语音通过",VideoMessage.class),
//    VIDEO_CALL(7, "视频通过", VideoMessage.class);

    private final int type;

    private final String desc;

    MessageTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static MessageTypeEnum valueOf(int type) {

        return Arrays.stream(MessageTypeEnum.values())
                .filter(messageTypeEnum -> messageTypeEnum.getType() == type)
                .findFirst()
                .orElse(null);
    }
}
