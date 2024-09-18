package com.king.im.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public interface MessageCallback {

    void handler(Consumer<Map<Long, ConcurrentHashMap<Integer, Channel>>> consumer);
}
