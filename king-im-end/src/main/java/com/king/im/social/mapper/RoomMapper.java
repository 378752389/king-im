package com.king.im.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.domain.SocialDTO;
import com.king.im.social.domain.SocialVO;
import com.king.im.social.domain.entity.Room;
import com.king.im.user.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomMapper extends BaseMapper<Room> {


    List<User> getUserList(@Param("roomId") Long roomId);

    IPage<Room> getRoomPage(IPage p, @Param("uid") Long uid);

    List<RoomDo> getRoomList(@Param("userId") Long userId);

    List<SocialVO> getSocialList(SocialDTO socialDTO);
}
