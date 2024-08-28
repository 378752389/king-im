package com.king.im.user.domain.enums;

import lombok.Getter;

@Getter
public enum OssUploadBusinessEnum {
    USER_AVATAR(1, "用户头像上传", ""),
    ROOM_AVATAR(2, "群头像上传", ""),
    MESSAGE(3, "消息文件", "message");

    private final Integer type;

    private final String desc;

    private final String path;

    OssUploadBusinessEnum(Integer type, String desc, String path) {
        this.type = type;
        this.desc = desc;
        this.path = path;
    }

    public static OssUploadBusinessEnum from(Integer type) {
        for (OssUploadBusinessEnum obe : OssUploadBusinessEnum.values()) {
            if (obe.type.equals(type)) {
                return obe;
            }
        }
        return null;
    }
}
