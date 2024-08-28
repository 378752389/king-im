package com.king.im.user.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.jwt.signers.AlgorithmUtil;
import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.user.domain.OssDTO;
import com.king.im.user.domain.OssVO;
import com.king.im.user.domain.enums.OssUploadBusinessEnum;
import com.king.im.user.service.OssService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OssServiceImpl implements OssService {

    @Resource
    private MinioClient minioClient;

    @Value("${king-im.oss.bucket}")
    private String bucket;

    @Value("${king-im.oss.url}")
    private String url;

    @Override
    @SneakyThrows
    public OssVO upload(OssDTO ossDTO) {
        Integer businessType = ossDTO.getBusinessType();
        OssUploadBusinessEnum obe = OssUploadBusinessEnum.from(businessType);
        if (obe == null) {
            throw new GlobalException("非法的业务类型");
        }
        Long uid = RequestInfoHolder.getUid();

        String absPath = String.valueOf(uid);
        if (!"".equals(obe.getPath())) {
            absPath += File.separator + obe.getPath();
        }

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String suffix = FileUtil.getSuffix(ossDTO.getFilename());
        String filename = uuid + "." + suffix;

        absPath += File.separator + filename;

        String uploadUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs
                        .builder()
                        .method(Method.PUT)
                        .bucket(bucket)
                        .expiry(1, TimeUnit.HOURS)
                        .object(absPath)
                        .build()
        );

        String downloadUrl = url + "/" + bucket + "/" + absPath;

        OssVO ossVO = new OssVO();
        ossVO.setUploadUrl(uploadUrl);
        ossVO.setDownloadUrl(downloadUrl);
        return ossVO;
    }
}
