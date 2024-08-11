package com.king.im.user.domain;

import lombok.Data;

/**
 * 初始建立连接的时候还没有uid，则存储空信息
 */
@Data
public class UserSessionDO {

    private Long uid;
}
