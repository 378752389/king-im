package com.king.im.social.controller;

import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.result.CommonResult;
import com.king.im.common.validator.Update;
import com.king.im.social.service.FriendService;
import com.king.im.user.domain.FriendDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("friend")
@Api(tags = "好友管理")
public class FriendController {
    @Resource
    private FriendService friendService;

    @GetMapping("list")
    @ApiOperation("好友列表")
    public CommonResult<List<FriendDO>> getRoomList() {
        Long uid = RequestInfoHolder.getUid();
        return CommonResult.ok(friendService.getFriendList(uid));
    }

    @PostMapping
    @ApiOperation("添加好友")
    public CommonResult<Integer> addFriend(Long friendId) {
        return CommonResult.ok(friendService.addFriend(friendId));
    }

    @PutMapping
    @ApiOperation("修改好友信息-备注信息")
    public CommonResult<Integer> modifyFriend(@Validated(Update.class) @RequestBody FriendDO friendDO) {
        return CommonResult.ok(friendService.updateFriend(friendDO));
    }

    @DeleteMapping
    @ApiOperation("删除好友")
    public CommonResult<Integer> deleteFriend(Long friendId) {
        return CommonResult.ok(friendService.deleteFriend(friendId));
    }

}
