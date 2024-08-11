package com.king.im.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String nickName;

    private String avatar;

    private String username;

    private String password;

    private Integer gender;

    private String sign;

    private Integer registerType;

    private String province;

    private String city;

    private String openId;

    private String lastLoginIp;

    private Date lastLoginTime;

    private Date createTime;
}
