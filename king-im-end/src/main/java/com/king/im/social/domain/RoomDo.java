package com.king.im.social.domain;

import com.king.im.common.validator.Update;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoomDo {


    private Long id;

    private Long userId;

    @NotNull(groups = {Update.class}, message = "房间号不能为空")
    private Long roomId;

    private String name;

    private Integer roomLimit;

    private String myName;

    private String notice;

    private String markName;

    private String createBy;

    private Long leaderId;

    private String createTime;

    private String avatar;

    private List<RoomMemberDo> roomMemberList;
}
