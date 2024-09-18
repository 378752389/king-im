package com.king.im.server.config;

import com.king.im.server.ServerBootstrap;

public class IMConfigProperties {

    private static final String messageChannel = "message:";
    private static final String messageResultChannel = "messageResult:";
    private static final String kickoffChannel = "kickoff:";


    public static String getMessageChannel() {
        return messageChannel + ServerBootstrap.getServerId();
    }

    public static String getMessageResultChannel() {
        return messageResultChannel + ServerBootstrap.getServerId();
    }

    public static String getKickoffChannel() {
        return kickoffChannel + ServerBootstrap.getServerId();
    }
}
