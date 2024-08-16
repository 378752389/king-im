package com.king.im.social.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_room")
public class UserRoomRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long roomId;

    /**
     * 群备注
     */
    private String markName;

    /**
     * 我在群内昵称
     */
    private String myName;

    /**
     * 入群时间
     */
    private Date createTime;
}
