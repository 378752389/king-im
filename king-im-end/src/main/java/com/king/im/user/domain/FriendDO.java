package com.king.im.user.domain;

import com.king.im.common.validator.Insert;
import com.king.im.common.validator.Update;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FriendDO {

    private Long id;

    private Long userId;

    @NotNull(groups = {Insert.class, Update.class}, message = "好友id不能未空")
    private Long peerId;

    private String peerAvatar;

    private String peerNickname;

    private String peerMarkName;

    private String sign;

    private Integer gender;

    private String province;

    private String city;
}
