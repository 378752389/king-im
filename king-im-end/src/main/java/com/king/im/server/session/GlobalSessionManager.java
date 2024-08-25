package com.king.im.server.session;

public interface GlobalSessionManager {

    /**
     * 判断设备是否在线
     * @param uid
     * @param terminal
     * @return
     */
    boolean isOnline(Long uid, Integer terminal);


    /**
     * 判断用户是否在线
     * @param uid
     * @return
     */
    boolean isOnline(Long uid);

    /**
     * session 续期
     * @param uid
     * @param terminal
     */
    void renewal(Long uid, Integer terminal);

    /**
     * 全局注册 session - 用户和服务器绑定关系
     * @param uid
     * @param terminal
     * @param serverId
     */
    void register(Long uid, Integer terminal, Long serverId);

    /**
     * 取消全局注册
     * @param uid
     * @param terminal
     */
    void unregister(Long uid, Integer terminal);
}
