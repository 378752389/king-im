package com.king.im.server.domain;

import lombok.Data;

@Data
public class Kickoff {

    private Long uid;

    private Integer terminal;

    /**
     * 强制下线原因
     */
    private String reason;
}
