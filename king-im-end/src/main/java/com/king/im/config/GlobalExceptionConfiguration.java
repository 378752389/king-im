package com.king.im.config;

import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionConfiguration {

    @ExceptionHandler(GlobalException.class)
    public CommonResult<Void> runtimeExceptionHandler(GlobalException e) {
        log.error("未知错误！", e);
        if (e.getMessage().equals("刷新token失效") || e.getMessage().equals("用户名或密码错误") || e.getMessage().equals("accessToken无效或过期")) {
            return CommonResult.unauth(e.getMessage());
        }

        return CommonResult.err(e.getMessage());
    }


}
