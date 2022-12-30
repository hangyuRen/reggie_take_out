package com.itheima.aop;


import com.itheima.annotation.CacheEvictBatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class CacheEvictBatchAspect {
    @Resource
    private CacheManager cacheManager;

    @Around("@annotation(com.itheima.annotation.CacheEvictBatch)")
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
