package com.king.im.user.service;

import com.king.im.user.domain.UserVO;

public interface UserService {

    UserVO info(Long uid);

    void modify(UserVO userVO);

    void changePassword(Long uid, String oldPwd, String newPwd);
}
