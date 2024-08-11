package com.king.im.social.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.domain.entity.Room;
import com.king.im.user.domain.entity.User;

import java.util.List;

public interface RoomService {

    List<User> getUserList(Long roomId);

    List<RoomDo> getRoomList(Long userId);

    int apply(Long roomId);

    void modifyRoom(RoomDo roomDo);

    IPage<Room> getRoomPage(Long uid, int num, int size);

    int inviteFriend(Long roomId, List<Long> friendIds);

    int createRoom(RoomDo roomDo);

    int quitRoom(Long roomId);

    int deleteRoom(Long roomId);
}
