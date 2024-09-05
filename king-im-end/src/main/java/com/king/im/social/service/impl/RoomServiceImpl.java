package com.king.im.social.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.king.im.client.IMSender;
import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.listener.event.RoomEvent;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.domain.entity.Room;
import com.king.im.social.domain.entity.UserFriendRelation;
import com.king.im.social.domain.entity.UserRoomRelation;
import com.king.im.social.mapper.RoomMapper;
import com.king.im.social.mapper.UserFriendMapper;
import com.king.im.social.mapper.UserRoomMapper;
import com.king.im.social.service.RoomService;
import com.king.im.user.domain.entity.User;
import com.king.im.user.mapper.UserMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private UserRoomMapper userRoomMapper;
    @Resource
    private UserFriendMapper userFriendMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Cacheable(cacheNames = "members", key = "#roomId")
    public List<User> getUserList(Long roomId) {
        return roomMapper.getUserList(roomId);
    }

    @Override
    public List<RoomDo> getRoomList(Long userId) {
        return roomMapper.getRoomList(userId);
    }

    @Override
    public int apply(Long roomId) {
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new GlobalException("房间不存在");
        }

        int result = 0;
        UserRoomRelation query = new UserRoomRelation();
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        Long uid = userInfo.getUid();
        String nickname = userInfo.getNickname();
        query.setUserId(uid);
        query.setRoomId(roomId);
        if (userRoomMapper.exists(Wrappers.query(query))) {
            throw new GlobalException("用户已经在房间内，不能重复申请");
        }

        UserRoomRelation urr = new UserRoomRelation();
        urr.setUserId(uid);
        urr.setRoomId(roomId);
        urr.setMarkName(room.getName());
        urr.setMyName(nickname);

        result += userRoomMapper.insert(urr);
        return result;
    }

    @Override
    @Transactional
    public void modifyRoom(RoomDo roomDo) {
        Long roomId = roomDo.getRoomId();

        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new GlobalException("房间不存在");
        }
        Long leaderId = room.getLeaderId();
        Long uid = RequestInfoHolder.getUid();
        if (leaderId.equals(uid)) {
            room.setNotice(roomDo.getNotice());
            room.setName(roomDo.getName());
            room.setAvatar(roomDo.getAvatar());
        }

        LambdaQueryWrapper<UserRoomRelation> query = Wrappers.<UserRoomRelation>lambdaQuery()
                .eq(UserRoomRelation::getRoomId, roomId)
                .eq(UserRoomRelation::getUserId, uid);

        UserRoomRelation userRoomRelation = userRoomMapper.selectOne(query);
        userRoomRelation.setMyName(roomDo.getMyName());
        userRoomRelation.setMarkName(roomDo.getMarkName());

        userRoomMapper.updateById(userRoomRelation);
        roomMapper.updateById(room);
    }


    @Override
    public IPage<Room> getRoomPage(Long uid, int num, int size) {
        IPage page = new Page(num, size);
        return roomMapper.getRoomPage(page, uid);
    }

    @Override
    @Transactional
    public int inviteFriend(Long roomId, List<Long> friendIds) {
        // 判断房间是否存在
        Room room = roomMapper.selectById(roomId);
        int result = 0;
        if (room == null) {
            throw new GlobalException("房间不存在");
        }

        if (CollUtil.isEmpty(friendIds)) {
            return result;
        }

        Long uid = RequestInfoHolder.getUid();
        LambdaQueryWrapper<UserFriendRelation> query = Wrappers.<UserFriendRelation>lambdaQuery()
                .eq(UserFriendRelation::getUserId, uid)
                .in(UserFriendRelation::getPeerId, friendIds);
        // 查询是否为好友关系
        List<UserFriendRelation> relations = userFriendMapper.selectList(query);
        List<Long> dbFriendIds = relations.stream().map(UserFriendRelation::getPeerId).collect(Collectors.toList());
        // 查询好友信息
        List<User> friends = userMapper.selectBatchIds(dbFriendIds);
        Map<Long, User> collect = friends.stream().collect(Collectors.toMap(User::getId, Function.identity()));


        for (UserFriendRelation relation : relations) {
            UserRoomRelation urr = new UserRoomRelation();

            urr.setUserId(relation.getPeerId());
            urr.setRoomId(roomId);
            // 好友已经在群内
            if (userRoomMapper.exists(Wrappers.query(urr))) {
                continue;
            }
            urr.setMarkName(room.getName());
            urr.setMyName(collect.get(relation.getPeerId()).getNickName());
            result += userRoomMapper.insert(urr);
        }
        return result;
    }

    @Override
    public long createRoom(RoomDo roomDo) {
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        RoomServiceImpl roomServiceImpl = (RoomServiceImpl) AopContext.currentProxy();

        long roomId = roomServiceImpl.createRoomBusiness(roomDo);
        List<User> userList = roomMapper.getUserList(roomId);
        String members = userList.stream().filter(user -> !user.getId().equals(userInfo.getUid())).map(User::getNickName).collect(Collectors.joining(", "));
        String notice = userInfo.getNickname() + " 创建了群聊；\n" + "邀请了 " + members + " 加入群聊；";
        applicationEventPublisher.publishEvent(new RoomEvent(this, roomId, userInfo.getTerminal(), notice));
        return roomId;
    }

    @Transactional
    public long createRoomBusiness(RoomDo roomDo) {
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        Room room = new Room();

        room.setName(roomDo.getName());
        room.setRoomLimit(200);
        room.setCreateBy(userInfo.getNickname());
        room.setLeaderId(userInfo.getUid());
        room.setNotice(roomDo.getNotice());
        room.setCreateTime(new Date());
        room.setAvatar(roomDo.getAvatar());

        roomMapper.insert(room);

        UserRoomRelation urr = new UserRoomRelation();
        urr.setId(0L);
        urr.setUserId(userInfo.getUid());
        urr.setRoomId(room.getId());
        urr.setMarkName(room.getName());
        urr.setMyName(userInfo.getNickname());

        userRoomMapper.insert(urr);

        return room.getId();
    }

    @Override
    public int quitRoom(Long roomId) {
        Long uid = RequestInfoHolder.getUid();
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new GlobalException("房间不存在");
        }
        Long leaderId = room.getLeaderId();
        if (leaderId.equals(uid)) {
            throw new GlobalException("群主不能退出房间");
        }

        UserRoomRelation urr = new UserRoomRelation();
        urr.setRoomId(roomId);
        urr.setUserId(uid);

        return userRoomMapper.delete(Wrappers.query(urr));
    }

    @Override
    @Transactional
    public int deleteRoom(Long roomId) {
        Long uid = RequestInfoHolder.getUid();
        Room room = roomMapper.selectById(roomId);
        if (room == null) {
            throw new GlobalException("房间不存在");
        }
        Long leaderId = room.getLeaderId();
        if (!leaderId.equals(uid)) {
            throw new GlobalException("不是群主，不能删除该聊天群");
        }

        int result = 0;
        result += roomMapper.deleteById(roomId);
        UserRoomRelation urr = new UserRoomRelation();
        urr.setRoomId(roomId);
        result += userRoomMapper.delete(Wrappers.query(urr));
        return result;
    }
}
