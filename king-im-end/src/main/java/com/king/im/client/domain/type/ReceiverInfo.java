package com.king.im.client.domain.type;

import com.king.im.common.enums.TerminalTypeEnum;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ReceiverInfo {

    /**
     * 消息接收方信息
     */
    private List<Long> receiverIds;

    /**
     * 接收者终端类型
     */
    private List<Integer> receiveTerminalTypes;

    public ReceiverInfo(Long receiverId) {
        this(Collections.singletonList(receiverId));
    }

    public ReceiverInfo(List<Long> receiverIds) {
        this(receiverIds, TerminalTypeEnum.all());
    }


    public ReceiverInfo(List<Long> receiverIds, List<Integer> receiveTerminalTypes) {
        this.receiverIds = receiverIds;
        this.receiveTerminalTypes = receiveTerminalTypes;
    }
}
