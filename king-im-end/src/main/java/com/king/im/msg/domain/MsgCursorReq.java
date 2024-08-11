package com.king.im.msg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgCursorReq {

    private Long roomId;

    private Long uid;

    private Long peerUid;

    private Long cursor;

    private Integer size;

}
