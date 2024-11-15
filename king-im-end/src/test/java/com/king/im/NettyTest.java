package com.king.im;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.king.im.common.log.mapper.LogMapper;
import com.king.im.common.utils.JwtUtils;
import com.king.im.common.utils.RedisUtils;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.msg.mapper.MsgMapper;
import com.king.im.msg.service.MessageService;
import com.king.im.server.protocol.data.ChatData;
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
    private MessageService messageService;

    @Resource
    private LogMapper logMapper;

    @Test
    public void test2() {
        List<ChatData> singleMsgList = msgMapper.getSingleMsgList(0L, 1L);
        System.out.println(singleMsgList);
//        ChatData chatMsg = msgMapper.getChatMsg(293L);
////        ChatData chatMsg = msgMapper.getChatMsg(372L);
//        System.out.println(chatMsg);
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
