package com.king.im.user.controller;

import cn.hutool.http.ContentType;
import com.king.im.common.result.CommonResult;
import com.king.im.user.domain.OssDTO;
import com.king.im.user.domain.OssVO;
import com.king.im.user.service.OssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("oss")
@RestController
@ApiOperation("minio-oss管理")
public class OssController {

    @Resource
    private OssService ossService;

    @PostMapping("upload")
    @ApiOperation("oss文件上传")
    public CommonResult<OssVO> upload(@RequestBody OssDTO ossDTO) {
        return CommonResult.ok(ossService.upload(ossDTO));
    }
}
