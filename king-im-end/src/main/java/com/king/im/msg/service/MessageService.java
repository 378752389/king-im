package com.king.im.msg.service;

import com.king.im.common.cursor.CursorResult;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.server.protocol.data.ChatData;

public interface MessageService {

    Long sendMsg(MsgReq req);

    ChatData getMsg(Long id);

    Long readedMsg(Long chatId, Integer type);

    CursorResult<ChatData> getHistoryMessageCursorPage(Long chatId, Integer chatType, Long cursor, Long size);

    void pullMessage(Long minMsgId, Long userId);

    void loadOfflineMessage(Long userId);

    CursorResult<ChatData> getMsgCursorPage(MsgCursorReq req);

    int updateMsgToSendStatus(Long msgId);

    int updateMsgToHasReadStatus(Long msgId);

    int updateMsgToRevokeStatus(Long msgId);
}
