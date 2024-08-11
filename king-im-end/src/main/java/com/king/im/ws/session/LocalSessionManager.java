package com.king.im.ws.session;

import io.netty.channel.ChannelHandlerContext;

public interface LocalSessionManager {

    /**
     * 缓存channel
     * @param ctx
     */
    void connect(ChannelHandlerContext ctx);

    /**
     * 移除 缓存 channel
     * @param ctx
     */
    void disconnect(ChannelHandlerContext ctx);

    /**
     * 登录
     * @param ctx
     * @param uid
     * @param terminalType
     */
    void online(ChannelHandlerContext ctx, Long uid, Integer terminalType);

    boolean isOnline(Long uid);

    /**
     * 下线
     * @param ctx
     * @param uid
     * @param terminalType
     * @return
     */
    boolean offline(ChannelHandlerContext ctx, Long uid, Integer terminalType);
}
