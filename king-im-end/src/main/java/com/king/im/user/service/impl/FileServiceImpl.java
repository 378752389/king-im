package com.king.im.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.common.log.SysLog;
import com.king.im.user.domain.FileVO;
import com.king.im.user.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${king-im.file.upload}")
    private String basePath;

    @Value("${king-im.file.download}")
    private String downloadPath;


    @Override
    @SysLog(description = "'上传文件：' + #file.getOriginalFilename() + ', 文件大小：' + #file.getSize() + 'bytes' + '文件下载路径：' + #ret.getUrl()")
    public FileVO upload(MultipartFile file) throws IOException {
        UserInfo userInfo = RequestInfoHolder.getUserInfo();
        String username = userInfo.getUsername();
        byte[] data = file.getBytes();
        String dateStr = DateUtil.format(new Date(), "yyyyMMdd");

        String genPath = String.join(File.separator, dateStr, username,String.join(".", SecureUtil.md5(new String(data)), FileUtil.getSuffix(file.getOriginalFilename())));
        File storeFile = new File(String.join(File.separator,
                basePath,
                genPath
        ));

        if (!FileUtil.exist(storeFile)) {
            FileUtil.mkParentDirs(storeFile);
        }
        try (FileOutputStream fos = new FileOutputStream(storeFile);) {
            fos.write(data);
        } catch (IOException e) {
            log.error("文件创建失败", e);
            return null;
        }

        FileVO fileVO = new FileVO();
        String path = String.join("/", dateStr, username,String.join(".", SecureUtil.md5(new String(data)), FileUtil.getSuffix(file.getOriginalFilename())));
        fileVO.setUrl(String.join("/", downloadPath, path));

        fileVO.setSize(file.getSize());
        fileVO.setName(file.getOriginalFilename());
        fileVO.setType(file.getContentType());

        return fileVO;
    }
}
