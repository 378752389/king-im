package com.king.im.server.protocol;

public class CMDType {
    public static final int PING = 1;
    public static final int PONG = 2;
    public static final int LOGIN = 3;
    public static final int LOGOUT = 4;
    public static final int CHAT = 5;

    public static final int LOADING = 6;

    public static final int NON_AUTH = -1;
}
