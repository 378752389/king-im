package com.king.im.user.service.impl;

import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.user.domain.UserVO;
import com.king.im.user.domain.entity.User;
import com.king.im.user.mapper.UserMapper;
import com.king.im.user.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Override
    @Cacheable(cacheNames = "user", key = "#uid")
    public UserVO info(Long uid) {
        User user = userMapper.selectById(uid);

        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setAvatar(user.getAvatar());
        userVO.setNickName(user.getNickName());
        userVO.setIsOnline(true);
        userVO.setCity(user.getCity());
        userVO.setIp(user.getLastLoginIp());
        userVO.setLoginTime(user.getLastLoginTime());

        return userVO;
    }

    @Override
    public void modify(UserVO userVO) {
        Long uid = RequestInfoHolder.getUid();

        User user = userMapper.selectById(uid);
        if (user == null) {
            throw new RuntimeException("用户信息不存在");
        }

        User add = new User();
        add.setNickName(userVO.getNickName());
        add.setAvatar(userVO.getAvatar());
        add.setCity(userVO.getCity());
        add.setId(uid);

        userMapper.updateById(add);
    }

    @Override
    public void changePassword(Long uid, String oldPwd, String newPwd) {
        User user = userMapper.selectById(uid);
        if (user == null) {
            throw new RuntimeException("用户信息不存在");
        }

        if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String encodePwd = passwordEncoder.encode(newPwd);
        user.setPassword(encodePwd);

        userMapper.updateById(user);
    }
}
