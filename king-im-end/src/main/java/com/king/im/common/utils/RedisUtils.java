package com.king.im.common.utils;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void expire(String key, Long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setnx(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void setex(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public void del(String pattern) {
        ScanOptions so = ScanOptions.scanOptions().build();

        Cursor<String> scan = redisTemplate.scan(so);

        redisTemplate.delete(pattern);
    }

    public void del(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public Cursor<String> scan(String pattern) {
        ScanOptions so = ScanOptions.scanOptions().match(pattern).build();
        return redisTemplate.scan(so);
    }


    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }


    public void zadd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public Set<Object> zrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Long zcard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public ZSetOperations.TypedTuple<Object> zpopmin(String key) {
        return redisTemplate.opsForZSet().popMin(key);
    }


    // ============================== list =====================================

    public void lpush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public Long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<Object> lrange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    // ============================== map =====================================

    public Object hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void hset(String key, Object hashKey, Object val) {
        redisTemplate.opsForHash().put(key, hashKey, val);
    }


    // ============================== set =====================================

    public Long scard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Long sadd(String key, Object val) {
        return redisTemplate.opsForSet().add(key, val);
    }

    public Long spop(String key, Object val) {
        return redisTemplate.opsForSet().remove(key, val);
    }
}
