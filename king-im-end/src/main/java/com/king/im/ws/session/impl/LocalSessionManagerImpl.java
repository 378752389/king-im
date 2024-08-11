package com.king.im.ws.session.impl;

import cn.hutool.core.collection.CollUtil;
import com.king.im.user.domain.UserSessionDO;
import com.king.im.ws.session.LocalSessionManager;
import com.king.im.ws.session.MessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 制作 channel 的存储管理，不会进行关闭
 */
@Service
@Slf4j
public class LocalSessionManagerImpl implements LocalSessionManager, MessageSender {

    /**
     * 所有连接都放入到该map中
     */
    private static final Map<Channel, UserSessionDO> ONLINE_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 登录用户放入到该map中
     */
    private static final Map<Long, ConcurrentHashMap<Integer, Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    @Override
    public void send(Consumer<Map<Long, ConcurrentHashMap<Integer, Channel>>> consumer) {
        consumer.accept(ONLINE_UID_MAP);
    }

    @Override
    public void connect(ChannelHandlerContext ctx) {
        ONLINE_SESSION_MAP.put(ctx.channel(), new UserSessionDO());
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx) {
        UserSessionDO userSessionDO = ONLINE_SESSION_MAP.get(ctx.channel());
        Long uid = userSessionDO.getUid();
        if (uid != null) {
            if (offline(ctx, uid, 1)) {
                // todo
                log.info("用户设备已全部下线");
            }
        }

        ONLINE_SESSION_MAP.remove(ctx.channel());
    }

    @Override
    public void online(ChannelHandlerContext ctx, Long uid, Integer terminalType) {
        UserSessionDO userSessionDO = Optional.of(uid).map(x -> {
            UserSessionDO usd = new UserSessionDO();
            usd.setUid(x);
            return usd;
        }).get();

        ONLINE_SESSION_MAP.put(ctx.channel(), userSessionDO);

        ONLINE_UID_MAP.putIfAbsent(uid, new ConcurrentHashMap<>());
        ConcurrentHashMap<Integer, Channel> map = ONLINE_UID_MAP.get(uid);

        map.compute(1, (k, v) -> {
            // 用户重复上线不受影响
            if (v != null && v != ctx.channel()) {
                log.error("旧设备被挤兑");
                v.close();
            }
            return ctx.channel();
        });

        log.info("uid: {}, channelId: {} 上线了", uid, ctx.channel().id());
    }

    @Override
    public boolean isOnline(Long uid) {
        ConcurrentHashMap<Integer, Channel> list = ONLINE_UID_MAP.get(uid);
        return CollUtil.isNotEmpty(list);
    }

    /**
     * 当前用户设备全部下线则返回 true
     *
     * @param ctx
     * @param uid
     * @param terminalType
     * @return
     */
    @Override
    public boolean offline(ChannelHandlerContext ctx, Long uid, Integer terminalType) {
        ONLINE_SESSION_MAP.get(ctx.channel()).setUid(null);

        ONLINE_UID_MAP.compute(uid, (k, v) -> {
            v.remove(terminalType);
            return v;
        });

        log.info("uid: {}, channelId: {} 下线了", uid, ctx.channel().id());

        return ONLINE_UID_MAP.get(uid).isEmpty();
    }

}
