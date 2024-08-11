package com.king.im.user.service;

public interface LoginCache {

    String getRefreshToken(Long uid);

    void setRefreshToken(Long uid, String refreshToken);
}
