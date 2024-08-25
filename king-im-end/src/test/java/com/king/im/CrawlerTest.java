package com.king.im;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CrawlerTest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                for (;;) {
                    Thread.sleep(1000);
                    log.info("任务执行完成");
                    throw new RuntimeException("中断线程");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
