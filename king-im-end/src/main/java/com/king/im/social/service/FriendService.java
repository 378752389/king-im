package com.king.im.social.service;

import com.king.im.user.domain.FriendDO;

import java.util.List;

public interface FriendService {

    List<FriendDO> getFriendList(Long uid);

    int addFriend(Long id);

    int deleteFriend(Long id);

    int updateFriend(FriendDO friendDO);

    List<FriendDO> queryFriend(Long id);
}
