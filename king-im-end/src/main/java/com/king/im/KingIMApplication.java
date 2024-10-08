package com.king.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.king.im.**.mapper")
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableAsync
public class KingIMApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingIMApplication.class, args);
    }
}
