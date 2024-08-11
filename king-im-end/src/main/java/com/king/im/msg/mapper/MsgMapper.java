package com.king.im.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.im.msg.domain.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgMapper extends BaseMapper<Msg> {
    List<Msg> getRoomMsgCursorPage(@Param("roomId") Long roomId, @Param("cursor") Long cursor, @Param("size") Integer size);

    List<Msg> getPeerMsgCursorPage(@Param("fromUid") Long fromUid, @Param("toUid") Long toUid, @Param("cursor") Long cursor, @Param("size") Integer size);


    List<Msg> getUnreadMsg(@Param("chatId") Long chatId, @Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 获取离线消息
     * @param minMsgId
     * @param userId
     * @return
     */
    List<Msg> getOfflineMsgByUserId(@Param("minMsgId") Long minMsgId, @Param("userId") Long userId);


    List<Msg> getOfflineMsgByRoomId(@Param("minMsgId") Long minMsgId, @Param("roomId") Long roomId);
    /**
     * 获取历史消息
     * @param maxMsgId
     * @param userId
     * @return
     */
    List<Msg> getHistoryMsg(@Param("minMsgId") Long maxMsgId, @Param("userId") Long userId);
}
