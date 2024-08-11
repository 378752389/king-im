package com.king.im.social.controller;

import com.king.im.common.result.CommonResult;
import com.king.im.social.domain.SocialDTO;
import com.king.im.social.domain.SocialVO;
import com.king.im.social.service.SocialSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("social")
@Api(tags = "社交管理")//swagger接口文档说明
public class SocialSearchController {


    @Resource
    private SocialSearchService socialSearchService;

    @GetMapping("search")
    @ApiOperation("搜索好友，搜索群公共接口")
    public CommonResult<List<SocialVO>> search(@Validated SocialDTO socialDTO) {
        return CommonResult.ok(socialSearchService.search(socialDTO));
    }
}
