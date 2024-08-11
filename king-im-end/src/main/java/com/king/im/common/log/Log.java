package com.king.im.common.log;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_log")
public class Log {

    private Long id;

    private String operator;

    private String log;

    private Date createTime;

    private String ip;
}
