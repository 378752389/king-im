package com.king.im.social.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_room")
public class Room {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer roomLimit;

    private String createBy;

    private Long leaderId;

    private String notice;

    private Date createTime;

    private String avatar;
}
