package com.king.im.server.protocol;

import lombok.Data;

@Data
public class CMD {

    private Integer cmd;

    private Object data;
}
