package com.king.im.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {

    private Long uid;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenTTL;

    private Long refreshTokenTTL;

    private String createTime;
}
