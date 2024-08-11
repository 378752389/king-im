package com.king.im.common.interceptor;

public class RequestInfoHolder {

    private static final ThreadLocal<Long> UID = new ThreadLocal<>();

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    public static Long getUid() {
        return UID.get();
    }

    public static void setUid(Long uid) {
        UID.set(uid);
    }

    public static void clear() {
        UID.remove();
        USER_INFO.remove();
    }

    public static UserInfo getUserInfo() {
        return USER_INFO.get();
    }

    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO.set(userInfo);
    }
}
