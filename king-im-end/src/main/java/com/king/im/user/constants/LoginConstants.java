package com.king.im.user.constants;

import java.time.Duration;

public class LoginConstants {

//    public static final long accessTokenTTL = Duration.ofMinutes(5).toMillis();
    public static final long accessTokenTTL = Duration.ofDays(5000).toMillis();

    public static final long refreshTokenTTL = Duration.ofDays(7).toMillis();

    public static final String accessSecret = "mini-king-access";

    public static final String refreshSecret = "mini-king-refresh";
}
