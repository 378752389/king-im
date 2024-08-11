package com.king.im.ws.session.impl;

import com.king.im.common.utils.RedisUtils;
import com.king.im.ws.session.GlobalSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GlobalSessionManagerImpl implements GlobalSessionManager {

    @Value("${king-im.server}")
    private String server;
    @Resource
    private RedisUtils redisUtils;

    private static final String USER_TERMINAL_STR = "user_terminal:%s:%s:server_id";


    @Override
    public Boolean isOnline(Long uid, Integer type) {
        String key = String.format(USER_TERMINAL_STR, uid, type);
        String serverId =(String) redisUtils.get(key);
        return serverId != null;
    }
}
