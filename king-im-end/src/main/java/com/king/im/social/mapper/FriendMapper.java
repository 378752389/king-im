package com.king.im.social.mapper;

import org.apache.ibatis.annotations.Param;

public interface FriendMapper {

    boolean isFriend(@Param("userId1") Long userId1,@Param("userId2") Long userId2);
}
