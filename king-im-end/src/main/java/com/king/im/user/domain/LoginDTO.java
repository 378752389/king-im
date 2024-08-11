package com.king.im.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("登录请求参数")
public class LoginDTO {

    @NotNull(message = "登录用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @NotNull(message = "登录密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @Min(value = 1, message = "登录终端类型取值错误")
    @Max(value = 3, message = "登录终端类型取值错误")
    @NotNull(message = "登录终端类型不能为空")
    @ApiModelProperty("登录终端类型")
    private Integer terminal;

}
