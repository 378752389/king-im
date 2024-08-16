package com.king.im.user.controller;

import com.king.im.common.result.CommonResult;
import com.king.im.user.domain.FileVO;
import com.king.im.user.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("file")
@Slf4j
@Api(tags = "文件管理")
public class FileController {


    @Resource
    private FileService fileService;

    @ApiOperation(value = "文件上传接口", notes = "用户选择文件上传")
    @PostMapping("upload")
    @ApiImplicitParam(name = "file", dataTypeClass = MultipartFile.class, value = "上传文件")
    public CommonResult<FileVO> upload(MultipartFile file) throws IOException {
        return CommonResult.ok(fileService.upload(file));
    }
}
