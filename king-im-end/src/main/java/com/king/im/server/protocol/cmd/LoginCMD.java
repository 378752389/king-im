package com.king.im.server.protocol.cmd;

import lombok.Data;

@Data
public class LoginCMD {

    private Integer terminal;

    private String accessToken;
}
