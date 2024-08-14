package com.king.im.msg.controller;

import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.result.CommonResult;
import com.king.im.msg.domain.MsgCursorReq;
import com.king.im.msg.domain.MsgReq;
import com.king.im.msg.service.HistoryMessageCache;
import com.king.im.msg.service.MessageService;
import com.king.im.server.protocol.data.ChatData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/msg")
@Api(tags = "消息管理")
public class MessageController {

    @Resource
    private MessageService messageService;
    @Resource
    private HistoryMessageCache historyMessageCache;

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
    public CommonResult<Void> recentMsg(MsgReq req) {
        return CommonResult.ok(null);
    }

    @GetMapping("/list")
    @ApiOperation("获取消息列表")
    public CommonResult<Void> msgList(MsgCursorReq req) {
        return CommonResult.ok(null);
    }

    @GetMapping("/pullOfflineMessage")
    @ApiOperation("拉取离线消息")
    public CommonResult<Void> pullOfflineMessage(Long minMsgId) {
        Long uid = RequestInfoHolder.getUid();
        messageService.loadOfflineMessage(minMsgId, uid);
        return CommonResult.ok(null);
    }

    @GetMapping("/pullHistoryMessage")
    @ApiOperation("拉取历史消息")
    public CommonResult<Void> pullHistoryMessage() {
        return CommonResult.ok(null);
    }


}
