package com.king.im.common.interceptor;

import lombok.Data;

@Data
public class UserInfo {

    private Long uid;

    private String nickname;

    private String username;

    private Integer terminal;
}
