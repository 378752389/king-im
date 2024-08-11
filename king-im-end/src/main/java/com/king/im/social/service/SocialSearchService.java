package com.king.im.social.service;

import com.king.im.social.domain.SocialDTO;
import com.king.im.social.domain.SocialVO;

import java.util.List;

public interface SocialSearchService {

    List<SocialVO> search(SocialDTO socialDTO);
}
