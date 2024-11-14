package com.king.im.common.domain.type;

import com.king.im.msg.domain.extra.Extra;
import lombok.Data;

@Data
public class ReferMsg {

    private Long referMsgId;

    private String nickname;

    private String content;

    private Integer contentType;

    private Extra extra;
}
