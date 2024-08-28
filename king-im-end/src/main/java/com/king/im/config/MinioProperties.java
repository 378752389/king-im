package com.king.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "king-im.oss")
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String url;

    private String bucket;
}
