package com.king.im.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class JSONUtils {

    @Resource
    private ObjectMapper objectMapper;

    // 序列化
    public String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("序列化异常: {}, object: {}", e.getMessage(), object);
            throw new RuntimeException();
        }
    }


    // 反序列化
    public <T> T parse(String s, T type) {
        try {
           return objectMapper.readValue(s, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            log.error("反序列化异常: {}, 数据: {}， 序列化目标对象：{}", e.getMessage(), s, type);
            throw new RuntimeException(e);
        }
    }

}
