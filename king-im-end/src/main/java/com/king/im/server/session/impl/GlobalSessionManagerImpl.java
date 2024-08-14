package com.king.im.server.session.impl;

import com.king.im.common.constants.RedisConstant;
import com.king.im.common.utils.RedisUtils;
import com.king.im.server.session.GlobalSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GlobalSessionManagerImpl implements GlobalSessionManager {

    @Value("${king-im.server}")
    private String server;
    @Resource
    private RedisUtils redisUtils;


    @Override
    public boolean isOnline(Long uid, Integer terminal) {
        String key = String.format(RedisConstant.USER_TERMINAL_STR, uid, terminal);
        String serverId =(String) redisUtils.get(key);
        return serverId != null;
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
        Long second = 60L;
        redisUtils.expire(key, second, TimeUnit.SECONDS);
        log.debug("uid: {}, terminal: {}, session续期 {}s", uid, terminal, second);
    }

}
