package com.king.im.server.ws;

import com.king.im.server.ChannelInfoHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.InetSocketAddress;

public class HttpHeadHandler extends ChannelInboundHandlerAdapter {

    private final static String IP = "ip";
    private final static String TOKEN = "token";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = socketAddress.getAddress().getHostAddress();
            ChannelInfoHolder.setIp(ctx.channel(), ip);

            ctx.pipeline().remove(this);
        }

        ctx.fireChannelRead(msg);
    }
}
