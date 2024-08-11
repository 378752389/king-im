package com.king.im.msg.service;

import com.king.im.common.cursor.CursorResult;
import com.king.im.sender.protocol.data.ChatData;

public interface OfflineMessageCache {

    void insert(ChatData chatData);

    CursorResult<ChatData> loadOfflineMessage(Long uid);

}
