package com.king.im.server.session.impl;

import com.king.im.common.constants.RedisConstant;
import com.king.im.common.utils.RedisUtils;
import com.king.im.server.session.GlobalSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GlobalSessionManagerImpl implements GlobalSessionManager {

    /**
     * 续期时间
     * <p>
     * 前端每5s发送一次心跳，后端每接受到5次心跳进行一次续期；
     * 超时60s（60s内心跳次数不足5次） session信息将丢失；
     * 前端超12s未发送消息，则触发读超时事件，断开连接；
     */
    private static final long RENEWAL_SECOND = 60;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public boolean isOnline(Long uid, Integer terminal) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        return !Objects.isNull(redisUtils.get(key));
    }

    @Override
    public boolean isOnline(Long uid) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, "*");
        Set<String> keys = redisUtils.keys(key);
        if (keys != null && keys.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void renewal(Long uid, Integer terminal) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        redisUtils.expire(key, RENEWAL_SECOND, TimeUnit.SECONDS);
        log.debug("uid: {}, terminal: {}, session续期 {}s", uid, terminal, RENEWAL_SECOND);
    }

    @Override
    public void register(Long uid, Integer terminal, Long serverId) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        redisUtils.setex(key, serverId, RENEWAL_SECOND, TimeUnit.SECONDS);
    }

    @Override
    public void unregister(Long uid, Integer terminal) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        redisUtils.del(key);
    }

    @Override
    public Long getServerId(Long uid, Integer terminal) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        Object o = redisUtils.get(key);
        if (o instanceof Integer) {
            return ((Integer) o).longValue();
        } else {
            return (Long) o;
        }
    }

    @Override
    public List<Long> getServerList(Long uid) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, "*");
        Set<String> keys = redisUtils.keys(key);
        if (keys != null && keys.size() > 0) {
            return keys.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
