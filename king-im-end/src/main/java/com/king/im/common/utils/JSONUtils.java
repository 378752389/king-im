package com.king.im.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.exceptions.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

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
            throw new RuntimeException(e);
        }
    }


    // 反序列化
    public <T> T parse(String s, Class<T> type) {
        try {
            Map map = objectMapper.readValue(s, Map.class);
            return objectMapper.convertValue(map, new TypeReference<T>() {
            });
        } catch (Exception e) {
            log.error("反序列化异常: {}, 数据: {}， 序列化目标对象：{}", e.getMessage(), s, type);
            throw new RuntimeException(e);
        }
    }

    public <T> T convert(Object o, TypeReference<T> reference) {
        return objectMapper.convertValue(o, reference);
    }

}
