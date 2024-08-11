package com.king.im.msg.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.king.im.msg.domain.extra.Extra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_msg", autoResultMap = true)
public class Msg implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roomId;

    private Long fromUid;

    private Long toUid;

    /**
     * 1-文本； 2-图片； 3-音频； 4-视频； 5-文件；
     */
    private Integer type;

    /**
     * 消息文本
     */
    private String content;

    /**
     * 消息状态  消息状态 1-待发送，2-已发送，3-已读
     */
    private Integer status;

    /**
     * 扩展信息
     */
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private Extra extra;


    private Date createTime;

    private Integer sendTerminal;

    private String atUids;

    private Long referMsgId;
}
