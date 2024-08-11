package com.king.im.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.social.domain.entity.UserFriendRelation;
import com.king.im.social.mapper.UserFriendMapper;
import com.king.im.social.service.FriendService;
import com.king.im.user.domain.FriendDO;
import com.king.im.user.domain.entity.User;
import com.king.im.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFriendMapper userFriendMapper;

    @Override
    public List<FriendDO> getFriendList(Long uid) {
        User user = userMapper.selectById(uid);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        return userMapper.getFriendList(uid);
    }

    @Override
    public int addFriend(Long id) {
        User user = userMapper.selectById(id);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        LambdaQueryWrapper<UserFriendRelation> query = Wrappers.<UserFriendRelation>lambdaQuery()
                .eq(UserFriendRelation::getUserId, userInfo.getUid())
                .eq(UserFriendRelation::getPeerId, user.getId());

        int result = 0;
        if (!userFriendMapper.exists(query)) {
            Date date = new Date();
            UserFriendRelation ufr1 = new UserFriendRelation();
            ufr1.setUserId(userInfo.getUid());
            ufr1.setPeerId(user.getId());
            ufr1.setCreateTime(date);
            ufr1.setMarkName(user.getNickName());

            result += userFriendMapper.insert(ufr1);

            UserFriendRelation ufr2 = new UserFriendRelation();
            ufr2.setUserId(user.getId());
            ufr2.setPeerId(userInfo.getUid());
            ufr2.setCreateTime(date);
            ufr2.setMarkName(userInfo.getNickname());

            result += userFriendMapper.insert(ufr2);
        }
        return result;
    }

    @Override
    public int deleteFriend(Long id) {
        User user = userMapper.selectById(id);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        Long uid = RequestInfoHolder.getUid();
        LambdaQueryWrapper<UserFriendRelation> query = Wrappers.<UserFriendRelation>lambdaQuery()
                .eq(UserFriendRelation::getUserId, uid)
                .eq(UserFriendRelation::getPeerId, user.getId());

        int result = 0;
        if (userFriendMapper.exists(query)) {
            UserFriendRelation ufr = new UserFriendRelation();
            ufr.setUserId(uid);
            ufr.setPeerId(user.getId());

            result += userFriendMapper.delete(Wrappers.query(ufr));

            ufr.setUserId(user.getId());
            ufr.setPeerId(uid);
            result += userFriendMapper.delete(Wrappers.query(ufr));
        }

        return result;
    }

    @Override
    public int updateFriend(FriendDO friendDO) {
        Long peerId = friendDO.getPeerId();
        User friend = userMapper.selectById(peerId);
        if (friend == null) {
            throw new RuntimeException("用户不存在");
        }
        Long userId = RequestInfoHolder.getUid();
        LambdaQueryWrapper<UserFriendRelation> query = Wrappers.<UserFriendRelation>lambdaQuery()
                .eq(UserFriendRelation::getUserId, userId)
                .eq(UserFriendRelation::getPeerId, peerId);

        UserFriendRelation relation = userFriendMapper.selectOne(query);
        if (relation == null) {
            throw new RuntimeException("你和对方不是朋友关系");
        }
        relation.setMarkName(friendDO.getPeerMarkName());
        return userFriendMapper.updateById(relation);
    }

    @Override
    public List<FriendDO> queryFriend(Long id) {
        User user = userMapper.selectById(id);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }

        return userMapper.getFriendList(id);
    }
}
