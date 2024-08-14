package com.king.im.msg.service.cache;

import com.king.im.common.cursor.CursorResult;
import com.king.im.common.utils.RedisUtils;
import com.king.im.msg.service.OfflineMessageCache;
import com.king.im.server.protocol.data.ChatData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OfflineMessageCacheImpl implements OfflineMessageCache {

    @Resource
    private RedisUtils redisUtils;

    private static final String OFFLINE_MSG_RELATION_SET = "offlineMsg:";
    private static final String OFFLINE_ROOM_KEY = "offlineMsg:room:";
    private static final String OFFLINE_PEER_KEY = "offlineMsg:peer:";

    private String getKey(Long uid) {
        return OFFLINE_MSG_RELATION_SET + uid;
    }

    private String getRoomKey(Long roomId, Long uid) {
        return OFFLINE_ROOM_KEY + roomId + ":" + uid;
    }

    private String getPeerKey(Long peerId, Long uid) {
        return OFFLINE_PEER_KEY + uid + ":" + peerId;
    }

    @Override
    public void insert(ChatData chatData) {

    }

    @Override
    public CursorResult<ChatData> loadOfflineMessage(Long uid) {
        return null;
    }

}
