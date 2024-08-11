package com.king.im.ws;


import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class RequestInfoHolder {

    private static final AttributeKey<String> IP = AttributeKey.newInstance("ip");
    private static final AttributeKey<String> TOKEN = AttributeKey.newInstance("token");

    private static final AttributeKey<Long> UID = AttributeKey.newInstance("uid");

    public static void setIp(Channel channel, String ip) {
        channel.attr(IP).set(ip);
    }

    public static void setToken(Channel channel, String token) {
        channel.attr(TOKEN).set(token);
    }

    public static void setUid(Channel channel, Long uid) {
        channel.attr(UID).set(uid);
    }

    public static String getIp(Channel channel) {
        return channel.attr(IP).get();
    }

    public static String getToken(Channel channel) {
        return channel.attr(TOKEN).get();
    }

    public static Long getUid(Channel channel) {
        return channel.attr(UID).get();
    }


}
