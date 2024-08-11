package com.king.im.social.domain;

import lombok.Data;

@Data
public class SocialVO {

    private Long id;

    private String avatar;

    private String name;

    /**
     * 1-联系人， 2-群
     */
    private Integer type;

    /**
     * type = 1 => 是否为好友关系； type = 2 => 是否已经加群
     */
    private Boolean bind;
}
