package com.king.im.common.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<>(200, "请求成功", data);
    }

    public static <T> CommonResult<T> err(String msg) {
        return new CommonResult<>(400, msg, null);
    }
    public static <T> CommonResult<T> unauth(String msg) {
        return new CommonResult<>(401, msg, null);
    }
}
