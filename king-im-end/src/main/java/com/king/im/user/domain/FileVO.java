package com.king.im.user.domain;

import lombok.Data;

@Data
public class FileVO {

    private String url;

    private String thumbUrl;

    private long size;

    private String name;

    private String type;
}
