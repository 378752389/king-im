package com.king.im.user.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.common.utils.JwtUtils;
import com.king.im.common.utils.RedisUtils;
import com.king.im.user.constants.LoginConstants;
import com.king.im.user.domain.LoginDTO;
import com.king.im.user.domain.LoginVO;
import com.king.im.user.domain.RegisterDTO;
import com.king.im.user.domain.entity.User;
import com.king.im.user.domain.enums.RegisterTypeEnum;
import com.king.im.user.mapper.UserMapper;
import com.king.im.user.service.LoginCache;
import com.king.im.user.service.LoginService;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;


@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private LoginCache loginCache;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisUtils redisUtils;




    @Override
    public void register(RegisterDTO registerDTO) {
        User usernameExisted = userMapper.getUserByUsername(registerDTO.getUsername());
        if (usernameExisted != null) {
            throw new GlobalException("用户名已存在");
        }
        User emailRegistered = userMapper.getUserByEmail(registerDTO.getEmail());
        if (emailRegistered != null) {
            throw new GlobalException("邮箱已经被注册");
        }

        User user = new User();

        user.setNickName("用户-" + registerDTO.getUsername());
        user.setAvatar("");
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRegisterType(RegisterTypeEnum.EMAIL.getType());
        user.setCreateTime(new Date());

        // todo
        registerBySimple(user);
    }

    public void registerByEmail(User add) {

    }

    public void registerBySimple(User add) {
        userMapper.insert(add);
    }

    @Override
    @SneakyThrows
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new GlobalException("用户名或密码错误");
        }

        String password = user.getPassword();
        if (!passwordEncoder.matches(loginDTO.getPassword(), password)) {
            throw new GlobalException("用户名或密码错误");
        }

        Date date = new Date();
        user.setLastLoginIp(request.getRemoteAddr());
        user.setLastLoginTime(date);
        user.setCity(parseIp2City(user.getLastLoginIp()));
        userMapper.updateById(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getId());
        userInfo.setNickname(user.getNickName());
        userInfo.setUsername(user.getUsername());
        userInfo.setTerminal(loginDTO.getTerminal());

        String userInfoStr = objectMapper.writeValueAsString(userInfo);

        String accessToken = JwtUtils.sign(user.getId(), userInfoStr, LoginConstants.accessTokenTTL, LoginConstants.accessSecret);
        String refreshToken = null;

        refreshToken = loginCache.getRefreshToken(user.getId());
        if (refreshToken == null) {
            refreshToken = JwtUtils.sign(user.getId(), userInfoStr, LoginConstants.refreshTokenTTL, LoginConstants.refreshSecret);
            loginCache.setRefreshToken(user.getId(), refreshToken);
        }

        // todo
        String createTimeStr = DateUtil.format(date, DatePattern.NORM_DATETIME_PATTERN);

        LoginVO loginVO = LoginVO.builder()
                .uid(user.getId())
                .accessToken(accessToken)
                .accessTokenTTL(LoginConstants.accessTokenTTL)
                .refreshToken(refreshToken)
                .refreshTokenTTL(LoginConstants.refreshTokenTTL)
                .createTime(createTimeStr)
                .build();

        return loginVO;
    }

    @Override
    public boolean validate(String token) {
        return JwtUtils.checkSign(token, LoginConstants.accessSecret);
    }

    @Override
    public Long parseToken(String token) {
        if (JwtUtils.checkSign(token, LoginConstants.accessSecret)) {
            return JwtUtils.getUserId(token);
        }

        return null;
    }

    @Override
    @SneakyThrows
    public LoginVO refresh(String refreshToken) {
        if (!JwtUtils.checkSign(refreshToken, LoginConstants.refreshSecret)) {
            throw new GlobalException("refresh token 校验失败, 请重新登录");
        }
        String userInfoStr = JwtUtils.getUserInfo(refreshToken);
        Long uid = JwtUtils.getUserId(refreshToken);
        String accessToken = JwtUtils.sign(uid, userInfoStr, LoginConstants.accessTokenTTL, LoginConstants.accessSecret);

        return LoginVO.builder()
                .uid(uid)
                .accessToken(accessToken)
                .accessTokenTTL(LoginConstants.accessTokenTTL)
                .refreshToken(refreshToken)
                .refreshTokenTTL(LoginConstants.accessTokenTTL)
                .createTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN))
                .build();
    }


    public String parseIp2City(String ip) {
        return "深圳";
    }


}
