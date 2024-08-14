package com.king.im.server.strategy;

import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.data.ChatData;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageStrategy implements MessageStrategy<ChatData> {
    @Override
    public void process(CMD cmd, ChannelHandlerContext ctx) {
        ChatData data = getRequest(cmd);

    }

    @Override
    public int getCmdType() {
        return CMDType.CHAT;
    }

    @Override
    public ChatData getRequest(CMD cmd) {
        return null;
    }
}
