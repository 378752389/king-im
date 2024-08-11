package com.king.im.user.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String avatar;

    private String nickName;

    private Boolean isOnline;

    private String city;

    private String ip;

    private Date loginTime;
}
