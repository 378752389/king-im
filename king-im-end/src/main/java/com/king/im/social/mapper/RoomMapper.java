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


    /**
     * 获取房间内用户列表
     *
     * @param roomId
     * @return
     */
    List<User> getUserList(@Param("roomId") Long roomId);

    Boolean isUserExist(@Param("roomId") Long roomId, @Param("userId") Long userId);

    /**
     * 分页获取用户加入的房间列表
     *
     * @param p
     * @param uid
     * @return
     */
    IPage<Room> getRoomPage(IPage p, @Param("uid") Long uid);

    /**
     * 获取用户加入对房间列表
     *
     * @param userId
     * @return
     */
    List<RoomDo> getRoomList(@Param("userId") Long userId);

    /**
     * 房间搜索
     *
     * @param socialDTO
     * @return
     */
    List<SocialVO> getSocialList(SocialDTO socialDTO);
}
