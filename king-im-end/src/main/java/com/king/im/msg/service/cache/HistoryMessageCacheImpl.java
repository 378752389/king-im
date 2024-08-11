package com.king.im.msg.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.utils.RedisUtils;
import com.king.im.msg.service.HistoryMessageCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HistoryMessageCacheImpl implements HistoryMessageCache {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ObjectMapper objectMapper;

    private static final String ROOM_MSG_ZSET = "room:";

    private static final String PEER_MSG_ZSET = "peer:";

    private static final Long FETCH_COUNT = 100L;


}
