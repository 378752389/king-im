package com.king.im.social.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.result.CommonResult;
import com.king.im.common.validator.Update;
import com.king.im.social.domain.RoomDo;
import com.king.im.social.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("room")
@Api(tags = "房间管理")
public class RoomController {
    @Resource
    private RoomService roomService;

    @GetMapping("list")
    @ApiOperation("获取用户加入的房间列表")
    public CommonResult<List<RoomDo>> getRoomList() {
        Long uid = RequestInfoHolder.getUid();
        return CommonResult.ok(roomService.getRoomList(uid));
    }

    @PostMapping("invite")
    @ApiOperation("邀请朋友进入房间")
    public CommonResult<Integer> inviteFriend(Long roomId, String friendIds) {
        List<String> list = StrUtil.split(friendIds, ",");
        List<Long> friendIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            friendIdList = list.stream().map(Long::valueOf).collect(Collectors.toList());
        }
        return CommonResult.ok(roomService.inviteFriend(roomId, friendIdList));
    }

    @GetMapping("apply")
    @ApiOperation("申请加入房间")
    public CommonResult<Integer> apply(Long roomId) {
        return CommonResult.ok(roomService.apply(roomId));
    }

//    @PostMapping("confirmApply")
//    @ApiOperation("确认申请加入房间")
//    public CommonResult<Integer> confirmApply() {
//        return null;
//    }

    @PostMapping
    @ApiOperation("创建房间")
    public CommonResult<Integer> createRoom(@RequestBody RoomDo roomDo) {
        return CommonResult.ok(roomService.createRoom(roomDo));
    }

    @PutMapping
    @ApiOperation("修改房间信息")
    public CommonResult<Void> modifyRoom(@Validated({Update.class}) @RequestBody RoomDo roomDo) {
        roomService.modifyRoom(roomDo);
        return CommonResult.ok(null);
    }

    @DeleteMapping("quit")
    @ApiOperation("退出房间")
    public CommonResult<Integer> quitRoom(Long roomId) {
        return CommonResult.ok(roomService.quitRoom(roomId));
    }

    @DeleteMapping
    @ApiOperation("删除房间")
    public CommonResult<Void> deleteRoom(Long roomId) {
        roomService.deleteRoom(roomId);
        return CommonResult.ok(null);
    }


}


//fetch("http://localhost:8080/room/list?uid=1")
