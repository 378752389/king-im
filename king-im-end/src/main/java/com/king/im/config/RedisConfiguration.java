package com.king.im.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        StringRedisSerializer ksr = new StringRedisSerializer();
        CustomRedisSerializer ksr = new CustomRedisSerializer();

        Jackson2JsonRedisSerializer<Object> vsr = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(ksr);
        redisTemplate.setValueSerializer(vsr);
        redisTemplate.setHashKeySerializer(ksr);
        redisTemplate.setHashValueSerializer(vsr);

        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

    static class CustomRedisSerializer extends StringRedisSerializer {

        private final String prefix = "chat:";

        @Override
        public String deserialize(byte[] bytes) {
            String s = bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
            if (StrUtil.isNotEmpty(s) && s.contains(prefix)) {
                s = s.substring(prefix.length());
            }

            return s;
        }

        @Override
        public byte[] serialize(String string) {
            if (StrUtil.isEmptyIfStr(string)) {
                return null;
            }
            return super.serialize(prefix + string);
        }
    }
}
