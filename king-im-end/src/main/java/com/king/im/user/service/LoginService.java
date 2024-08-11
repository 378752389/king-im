package com.king.im.user.service;

import com.king.im.user.domain.LoginDTO;
import com.king.im.user.domain.LoginVO;

public interface LoginService {

    Void register(LoginDTO loginDTO);

    LoginVO login(LoginDTO loginDTO);

    boolean validate(String token);

    Long parseToken(String token);

    LoginVO refresh(String refreshToken);
}
