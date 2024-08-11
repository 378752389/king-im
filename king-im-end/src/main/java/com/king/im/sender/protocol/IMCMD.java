package com.king.im.sender.protocol;

import lombok.Data;

@Data
public class IMCMD<T> {

    private Integer cmd;

    private T data;
}
