package com.king.im.server.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.king.im.server.protocol.CMD;
import io.netty.channel.ChannelHandlerContext;

public interface MessageStrategy<T> {

    void process(CMD cmd, ChannelHandlerContext ctx);

    int getCmdType();

    T getRequest(CMD cmd);
}
