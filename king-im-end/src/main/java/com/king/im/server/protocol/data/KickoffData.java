package com.king.im.server.protocol.data;

import lombok.Data;

@Data
public class KickoffData {

    /**
     * 强制下线原因
     */
    private String reason;
}
