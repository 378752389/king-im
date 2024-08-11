package com.king.im.ws;

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

            String token = request.headers().get("token");
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = socketAddress.getAddress().getHostAddress();

            RequestInfoHolder.setIp(ctx.channel(), ip);
            RequestInfoHolder.setToken(ctx.channel(), token);

            ctx.pipeline().remove(this);
        }

        ctx.fireChannelRead(msg);
    }
}
