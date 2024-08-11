package com.king.im.social.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class SocialDTO {

    @Range(min = 1, max = 2, message = "type 查询类型取值必须为 1 （id） 或 2（昵称）")
    private Integer type;

    @Range(min = 1, max = 2, message = "type 查询方式取值必须为 1 （联系人） 或 2 （群）")
    private Integer method;

    @NotNull(message = "查询信息不能为空")
    private String text;

    @Null(message = "userId必须为空")
    private Long userId;
}
