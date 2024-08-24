package com.king.im.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.im.social.domain.SocialDTO;
import com.king.im.social.domain.SocialVO;
import com.king.im.user.domain.FriendDO;
import com.king.im.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getUserByUsername(@Param("username") String username);

    User getUserByEmail(@Param("email") String email);

    List<FriendDO> getFriendList(@Param("userId") Long userId);

    List<SocialVO> getSocialList(SocialDTO socialDTO);
}
