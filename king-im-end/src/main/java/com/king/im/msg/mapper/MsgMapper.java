package com.king.im.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.im.msg.domain.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgMapper extends BaseMapper<Msg> {
    /**
     * 分页获取群聊消息
     * @param roomId
     * @param cursor
     * @param size
     * @return
     */
    List<Msg> getRoomMsgCursorPage(@Param("roomId") Long roomId, @Param("cursor") Long cursor, @Param("size") Long size);

    /**
     * 分页获取单聊消息
     * @param peer1Id
     * @param peer2Id
     * @param cursor
     * @param size
     * @return
     */
    List<Msg> getPeerMsgCursorPage(@Param("peer1Id") Long peer1Id, @Param("peer2Id") Long peer2Id, @Param("cursor") Long cursor, @Param("size") Long size);


    List<Msg> getUnreadMsg(@Param("chatId") Long chatId, @Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 获取离线私聊消息
     * @param userId
     * @return
     */
    List<Msg> getOfflineSingleMsgList(@Param("userId") Long userId);

    /**
     * 获取离线群聊消息
     * @param roomId
     * @return
     */
    List<Msg> getOfflineRoomMsgList(@Param("roomId") Long roomId);

    /**
     * 获取消息状态为已发送的消息，拉取至多1000条私聊消息
     * @param minMsgId
     * @param userId
     * @return
     */
    List<Msg> getSingleMsgList(@Param("minMsgId") Long minMsgId, @Param("userId") Long userId);

    /**
     * 拉群至多100条群聊消息
     * @param minMsgId
     * @param roomId
     * @return
     */
    List<Msg> getRoomMsgList(@Param("minMsgId") Long minMsgId, @Param("roomId") Long roomId);


    /**
     * 获取历史消息
     * @param maxMsgId
     * @param userId
     * @return
     */
    List<Msg> getHistoryMsg(@Param("minMsgId") Long maxMsgId, @Param("userId") Long userId);
}
