package com.king.im.server;


import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class ChannelInfoHolder {

    private static final AttributeKey<String> IP = AttributeKey.newInstance("ip");
    private static final AttributeKey<Integer> TERMINAL = AttributeKey.newInstance("terminal");

    private static final AttributeKey<Integer> HEARTBEAT_TIME = AttributeKey.newInstance("heartbeat_time");

    private static final AttributeKey<Long> UID = AttributeKey.newInstance("uid");

    public static void setHeartbeatTime(Channel channel, Integer times) {
        channel.attr(HEARTBEAT_TIME).set(times);
    }

    public static void setTerminal(Channel channel, Integer terminal) {
        channel.attr(TERMINAL).set(terminal);
    }

    public static Integer getHeartbeatTime(Channel channel) {
        return channel.attr(HEARTBEAT_TIME).get();
    }

    public static Integer getTerminal(Channel channel) {
        return channel.attr(TERMINAL).get();
    }

    public static void setIp(Channel channel, String ip) {
        channel.attr(IP).set(ip);
    }


    public static void setUid(Channel channel, Long uid) {
        channel.attr(UID).set(uid);
    }

    public static String getIp(Channel channel) {
        return channel.attr(IP).get();
    }

    public static Long getUid(Channel channel) {
        return channel.attr(UID).get();
    }


}
