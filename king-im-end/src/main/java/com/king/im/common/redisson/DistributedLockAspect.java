package com.king.im.common.redisson;

import com.king.im.common.exceptions.GlobalException;
import com.king.im.common.utils.SpringELUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class DistributedLockAspect {

    @Resource
    private RedissonClient redissonClient;

    @Around("@annotation(com.king.im.common.redisson.DistributedLock)")
    public Object doLock(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String lockKey = distributedLock.lockKey();
        long leaseTime = distributedLock.leaseTime();
        long waitTime = distributedLock.waitTime();
        TimeUnit unit = distributedLock.unit();

        String key = SpringELUtils.parseSpringEL(method, args, lockKey);
        log.debug("加分布式锁: el: {}, key: {}", lockKey, key);
        RLock lock = redissonClient.getLock(key);
        try {
            boolean locked = lock.tryLock(waitTime, leaseTime, unit);
            if (locked) {
                return pjp.proceed();
            } else {
                throw new GlobalException("加锁失败");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
