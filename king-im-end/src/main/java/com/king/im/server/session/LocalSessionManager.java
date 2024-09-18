package com.king.im.server.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public interface LocalSessionManager {

    /**
     * 获取 context
     * @param uid
     * @param terminal
     * @return
     */
    Channel getChannel(Long uid, Integer terminal);

    /**
     * 缓存channel
     * @param ctx
     */
    void connect(ChannelHandlerContext ctx);

    /**
     * 移除 channel
     * @param ctx
     */
    void disconnect(ChannelHandlerContext ctx);

    /**
     * 上线
     * @param ctx
     * @param uid
     * @param terminalType
     */
    void online(ChannelHandlerContext ctx, Long uid, Integer terminalType, String username);

    /**
     * 下线
     * @param ctx
     * @param uid
     * @param terminalType
     * @return
     */
    boolean offline(ChannelHandlerContext ctx, Long uid, Integer terminalType);

    boolean offline(Channel channel, Long uid, Integer terminalType);

    /**
     * 用户全局 session 进行续期
     * @param userId
     * @param terminal
     */
    void renewal(Long userId, Integer terminal);
}
