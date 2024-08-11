package com.king.im.msg.domain;

import com.king.im.msg.domain.extra.Extra;
import lombok.Data;

import java.util.List;

@Data
public class MsgReq {

    private Long roomId;

    private Long toUid;

    private Integer msgType;

    private String text;

    private Long referMsgId;

    private List<Long> atUids;

    private Extra extra;
}
