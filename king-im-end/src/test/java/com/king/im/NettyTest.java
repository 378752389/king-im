package com.king.im;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.king.im.common.log.mapper.LogMapper;
import com.king.im.common.utils.JwtUtils;
import com.king.im.common.utils.RedisUtils;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.msg.mapper.MsgMapper;
import com.king.im.social.service.RoomService;
import com.king.im.user.domain.entity.User;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class NettyTest {

    @Autowired
    private MsgMapper msgMapper;
    @Resource
    private RoomService roomService;

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LogMapper logMapper;

    @Test
    public void test2() {
        List<String> keys = Lists.newArrayList("chat:test:1:1", "chat:test:2:2", "chat:test:3:3", "chat:test:4:1");
        List<String> objects = stringRedisTemplate.opsForValue().multiGet(keys);
        for (Object object : objects) {
            System.out.println(object);
        }
    }

    @Data
    public static class A {
        private Integer id;
        private String name;
    }

    public static void main(String[] args) {
        String sign = JwtUtils.sign(1L, "", 1000 * 60 * 60 * 24 * 5000L, "mini-king");
        System.out.println(sign);

        ObjectMapper objectMapper = new ObjectMapper();

        String s  = "asd";

        Map<String, Object> o = new HashMap<>();
        o.put("type", "火车");
        o.put("brand", "volvo");
        o.put("name", "x90");

        A a = objectMapper.convertValue(o, new TypeReference<A>() {
        });

        System.out.println(a);

    }


    @Test
    public void test1() {
        List<User> userList = roomService.getUserList(1L);
        System.out.println("人数量：" + userList.size());
        roomService.getUserList(1L);
    }


}
