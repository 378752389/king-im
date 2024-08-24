package com.king.im.config;

import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionConfiguration {

    @ExceptionHandler(GlobalException.class)
    public CommonResult<Void> globalExceptionHandler(GlobalException e) {
//        if (e.getMessage().equals("刷新token失效") || e.getMessage().equals("accessToken无效或过期")) {
//            return CommonResult.unauth(e.getMessage());
//        }
        return CommonResult.err(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<Void> methodParamExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        Object target = e.getTarget();
        List<FieldError> fieldErrors = e.getFieldErrors();
        String uri = request.getRequestURI();
        String msg = fieldErrors.get(0).getDefaultMessage();
        String validResult = IntStream.range(0, fieldErrors.size())
                .mapToObj(index -> {
                    FieldError error = fieldErrors.get(index);
                    return String.join("", String.valueOf(index + 1), ". ", error.getField(), "=>", error.getDefaultMessage(), "; ");
                }).reduce((acc, cur) -> acc + cur).orElse("");
        log.warn("请求异常；路径：{} 校验异常结果: {} 参数：{}", uri, validResult, target);
        return CommonResult.err(msg);
    }

    @ExceptionHandler(RuntimeException.class)
    public void unknownExceptionHandler(RuntimeException e) {
        log.error("未知错误！", e);
    }


}
