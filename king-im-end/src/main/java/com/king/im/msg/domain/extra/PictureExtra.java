package com.king.im.msg.domain.extra;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureExtra implements Serializable {

    private Long size;

    private String name;

    private String type;

    private String url;
}
