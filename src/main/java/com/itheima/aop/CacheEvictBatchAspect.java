package com.itheima.aop;


import com.itheima.annotation.CacheEvictBatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class CacheEvictBatchAspect {
    @Autowired
    private CacheManager cacheManager;

    @Pointcut("@annotation(com.itheima.annotation.CacheEvictBatch)")
    public void cacheEvictBatch(){
    }

    @Around("cacheEvictBatch()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheEvictBatch annotation = method.getAnnotation(CacheEvictBatch.class);
        String value = annotation.value();
        String[] keys = annotation.keys();
        Cache nativeCache = (Cache)Objects.requireNonNull(cacheManager.getCache(value)).getNativeCache();
        for(String key : keys) {
            nativeCache.evictIfPresent(key);
        }
        return joinPoint.proceed();
    }
}