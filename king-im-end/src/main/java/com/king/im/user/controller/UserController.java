package com.king.im.user.controller;

import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.result.CommonResult;
import com.king.im.social.service.RoomService;
import com.king.im.user.domain.UserVO;
import com.king.im.user.domain.entity.User;
import com.king.im.user.service.UserService;
import com.king.im.ws.session.LocalSessionManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class UserController {
    @Resource
    private RoomService roomService;
    @Resource
    private LocalSessionManager sessionManager;
    @Resource
    private UserService userService;

    @GetMapping("info")
    @ApiOperation("获取用户信息")
    public CommonResult<UserVO> info() {
        Long uid = RequestInfoHolder.getUid();
        if (uid == null) {
            return CommonResult.err("请先进行登录");
        }
        return CommonResult.ok(userService.info(uid));
    }


    @PostMapping("modify")
    @ApiOperation("修改用户信息")
    public CommonResult<UserVO> modifyUserInfo(@RequestBody UserVO userVO) {
        userService.modify(userVO);
        return CommonResult.ok(null);
    }

    @PostMapping("password")
    @ApiOperation("修改用户密码")
    public CommonResult<Void> changePassword(String oldPwd, String newPwd) {
        Long uid = RequestInfoHolder.getUid();
        userService.changePassword(uid, oldPwd, newPwd);
        return CommonResult.ok(null);
    }

    @GetMapping("roomList")
    @ApiOperation("获取房间内用户在线状态")
    public Map<String, Object> onlineRoom(Long roomId) {
        List<User> userList = roomService.getUserList(roomId);

        Map<Boolean, List<UserVO>> userMap = userList.stream().map(user -> {
            UserVO vo = new UserVO();

            vo.setId(user.getId());
            vo.setAvatar(user.getAvatar());
            vo.setNickName(user.getNickName());

            vo.setIsOnline(sessionManager.isOnline(vo.getId()));
            vo.setCity(user.getCity());
            vo.setIp(user.getLastLoginIp());
            vo.setLoginTime(user.getLastLoginTime());

            return vo;
        }).collect(Collectors.groupingBy(UserVO::getIsOnline));

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "请求成功");
        map.put("data", userMap);

        return map;
    }

}
