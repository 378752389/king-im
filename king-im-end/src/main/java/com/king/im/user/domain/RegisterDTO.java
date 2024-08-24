package com.king.im.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("注册请求参数")
public class RegisterDTO {

    @Email(message = "邮箱格式错误")
    @NotNull(message = "邮箱不能为空")
    @ApiModelProperty("注册邮箱")
    private String email;

    @NotNull(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度必须在 2 - 20 个字符之间")
    @ApiModelProperty("用户名")
    private String username;

    @NotNull(message = "密码不能为空")
    @Length(min = 3, max = 20, message = "密码长度必须在 2- 20 个字符之间")
    @ApiModelProperty("密码")
    private String password;
}
