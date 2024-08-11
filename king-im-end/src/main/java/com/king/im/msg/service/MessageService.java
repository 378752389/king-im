package com.king.im.msg.service;

import com.king.im.common.cursor.CursorResult;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.sender.protocol.data.ChatData;

import java.util.List;

public interface MessageService {

    Long sendMsg(MsgReq req);

    ChatData getMsg(Long id);

    Long readedMsg(Long chatId, Integer type);

    List<ChatData> loadHistoryMessage(MsgReq req);

    void loadOfflineMessage(Long minMsgId, Long userId);

    CursorResult<ChatData> getMsgCursorPage(MsgCursorReq req);

    int updateMsgToSendStatus(Long msgId);

    int updateMsgToHasReadStatus(Long msgId);

    int updateMsgToRevokeStatus(Long msgId);
}
