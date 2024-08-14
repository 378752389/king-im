package com.king.im.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public interface MessageSender {

    public void send(Consumer<Map<Long, ConcurrentHashMap<Integer, Channel>>> consumer);
}
