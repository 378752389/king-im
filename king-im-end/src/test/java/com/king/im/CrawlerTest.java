package com.king.im;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CrawlerTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String a = "";
        String join = String.join(File.separator, "b", "", "");
        System.out.println(join);

        String join1 = StrUtil.join(File.separator, "", "");
        System.out.println(join1);
    }
}
