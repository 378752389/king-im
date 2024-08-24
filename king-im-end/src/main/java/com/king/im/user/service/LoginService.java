package com.king.im.user.service;

import com.king.im.user.domain.LoginDTO;
import com.king.im.user.domain.LoginVO;
import com.king.im.user.domain.RegisterDTO;

public interface LoginService {

    void register(RegisterDTO registerDTO);

    LoginVO login(LoginDTO loginDTO);

    boolean validate(String token);

    Long parseToken(String token);

    LoginVO refresh(String refreshToken);
}
