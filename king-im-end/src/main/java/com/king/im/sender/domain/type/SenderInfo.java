package com.king.im.sender.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SenderInfo {

    /**
     * 发送方用户id
     */
    private Long uid;

    /**
     * 接收方用户id
     */
    private Integer terminal;
}
