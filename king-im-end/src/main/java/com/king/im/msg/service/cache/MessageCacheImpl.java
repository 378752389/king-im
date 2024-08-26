package com.king.im.msg.service.cache;

import com.king.im.common.utils.RedisUtils;
import com.king.im.msg.service.MessageCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageCacheImpl implements MessageCache {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_MESSAGE_POSITION_MAP = "room_message_position:%s";

    @Override
    public Long getRoomMessagePosition(Long roomId, Long userId) {
        String key = String.format(ROOM_MESSAGE_POSITION_MAP, roomId);
        String o = (String) redisTemplate.opsForHash().get(key, String.valueOf(userId));
        if (o != null) {
            return Long.parseLong(o);
        }
        return null;
    }

    @Override
    public void setRoomMessagePosition(Long roomId, Long userId, Long position) {
        String key = String.format(ROOM_MESSAGE_POSITION_MAP, roomId);
        redisTemplate.opsForHash().put(key, String.valueOf(userId), Long.toString(position));
    }
}
