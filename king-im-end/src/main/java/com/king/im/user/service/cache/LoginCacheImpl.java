package com.king.im.user.service.cache;

import com.king.im.common.utils.RedisUtils;
import com.king.im.user.constants.LoginConstants;
import com.king.im.user.service.LoginCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class LoginCacheImpl implements LoginCache {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public String getRefreshToken(Long uid) {
        return (String) redisUtils.get(getKey(uid));
    }

    @Override
    public void setRefreshToken(Long uid, String refreshToken) {
        redisUtils.setex(getKey(uid), refreshToken, LoginConstants.refreshTokenTTL, TimeUnit.MILLISECONDS);
    }

    private String getKey(Long uid) {
        return "refreshToken:" + uid;
    }


}
