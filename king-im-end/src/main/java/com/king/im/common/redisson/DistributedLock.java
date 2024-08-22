package com.king.im.common.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 锁默认进行续期，每10s续期一次
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 锁key
     * @return
     */
    String lockKey();

    /**
     * 获取锁等待时间
     * @return
     */
    long waitTime() default 5;

    /**
     * 锁持续时间
     * @return
     */
    long leaseTime() default 30;

    /**
     * 时间单位
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}
