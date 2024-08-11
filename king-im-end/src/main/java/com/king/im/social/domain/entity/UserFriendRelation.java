package com.king.im.social.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_friend")
public class UserFriendRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long peerId;

    private Date createTime;

    private String markName;

}
