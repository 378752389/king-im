package com.king.im.social.service.impl;

import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.social.domain.SocialDTO;
import com.king.im.social.domain.SocialVO;
import com.king.im.social.mapper.RoomMapper;
import com.king.im.social.service.SocialSearchService;
import com.king.im.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SocialSearchServiceImpl implements SocialSearchService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoomMapper roomMapper;

    @Override
    public List<SocialVO> search(SocialDTO socialDTO) {
        Long uid = RequestInfoHolder.getUid();
        socialDTO.setUserId(uid);
        List<SocialVO> socialList = new ArrayList<>();
        if (socialDTO.getMethod() == 1) {
            socialList = userMapper.getSocialList(socialDTO);
        } else {
            socialList = roomMapper.getSocialList(socialDTO);
        }
        return socialList;
    }
}
