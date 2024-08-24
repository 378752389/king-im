package com.king.im.user.controller;

import com.king.im.common.result.CommonResult;
import com.king.im.user.domain.LoginDTO;
import com.king.im.user.domain.LoginVO;
import com.king.im.user.domain.RegisterDTO;
import com.king.im.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录管理")
public class LoginController {
    @Resource
    private LoginService loginService;


    @PostMapping("login")
    @ApiOperation("登录")
    public CommonResult<LoginVO> login(@Validated LoginDTO loginDTO) {
        return CommonResult.ok(loginService.login(loginDTO));
    }

    @PostMapping("register")
    @ApiOperation("注册")
    public CommonResult register(@Validated @RequestBody RegisterDTO registerDTO) {
        loginService.register(registerDTO);
        return CommonResult.ok(null);
    }

    @PostMapping("refreshToken")
    @ApiOperation("刷新token")
    public CommonResult<LoginVO> refresh(String refreshToken) {
        return CommonResult.ok(loginService.refresh(refreshToken));
    }

    @PostMapping("logout")
    @ApiOperation("注销登录")
    public CommonResult refresh() {
        return CommonResult.ok(null);
    }

}
