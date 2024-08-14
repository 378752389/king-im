package com.king.im.server.ws.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.server.protocol.CMD;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class IMWebSocketEncoder extends MessageToMessageEncoder<CMD> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CMD CMD, List<Object> list) throws Exception {
        ObjectMapper om = new ObjectMapper();
        String jsonText = om.writeValueAsString(CMD);
        TextWebSocketFrame frame = new TextWebSocketFrame(jsonText);
        list.add(frame);
    }
}
