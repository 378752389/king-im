package com.king.im.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.im.social.domain.entity.UserRoomRelation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoomMapper extends BaseMapper<UserRoomRelation> {
}
