package com.king.im.msg.controller;

import com.king.im.common.cursor.CursorResult;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.result.CommonResult;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.msg.service.MessageService;
import com.king.im.server.protocol.data.ChatData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/msg")
@Api(tags = "消息管理")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/send")
    @ApiOperation("发送消息")
    public CommonResult<ChatData> sendMsg(@RequestBody MsgReq req) {
        Long l = messageService.sendMsg(req);
        ChatData msg = messageService.getMsg(l);
        return CommonResult.ok(msg);
    }

    @PostMapping("/readed")
    @ApiOperation("消息已读")
    public CommonResult<Long> readedMsg(Long chatId, Integer type) {
        Long l = messageService.readedMsg(chatId, type);
        return CommonResult.ok(l);
    }

    @GetMapping("/recent")
    @ApiOperation("获取近期聊天消息")
    public CommonResult recentMsg(MsgReq req) {
        return CommonResult.ok(null);
    }

    @GetMapping("/list")
    @ApiOperation("获取消息列表")
    public CommonResult msgList(MsgCursorReq req) {
        return CommonResult.ok(null);
    }

    @GetMapping("/pullMessage")
    @ApiOperation("拉群消息")
    public CommonResult<Void> pullMessage(Long minMsgId) {
        Long uid = RequestInfoHolder.getUid();
        messageService.pullMessage(minMsgId, uid);
        return CommonResult.ok(null);
    }

    @GetMapping("/pullOfflineMessage")
    @ApiOperation("拉取离线消息")
    public CommonResult pullOfflineMessage() {
        Long uid = RequestInfoHolder.getUid();
        messageService.loadOfflineMessage(uid);
        return CommonResult.ok(null);
    }

    @GetMapping("/history")
    @ApiOperation("查询聊天记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "chatId", dataType = "java.lang.Long", value = "会话id：单聊-好友id，群聊-房间id"),
            @ApiImplicitParam(name = "chatType", dataType = "java.lang.Long", value = "会话类型：1-单聊，2-群聊"),
            @ApiImplicitParam(name = "cursor", dataType = "java.lang.Long", value = "页码"),
            @ApiImplicitParam(name = "size", dataType = "java.lang.Long", value = "页码大小"),
    })
    public CommonResult<CursorResult<ChatData>> historyMessage(@NotNull(message = "会话id不能为空") Long chatId,
                                                               @NotNull(message = "会话类型不能为空") Integer chatType,
                                                               Long cursor,
                                                               Long size) {
        return CommonResult.ok(messageService.getHistoryMessageCursorPage(chatId, chatType, cursor, size));
    }


}
