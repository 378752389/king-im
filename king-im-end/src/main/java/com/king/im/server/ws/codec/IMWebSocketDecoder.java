package com.king.im.server.ws.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.server.protocol.CMD;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class IMWebSocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        ObjectMapper om = new ObjectMapper();
        CMD cmd = om.readValue(textWebSocketFrame.text(), CMD.class);
        list.add(cmd);
    }
}
