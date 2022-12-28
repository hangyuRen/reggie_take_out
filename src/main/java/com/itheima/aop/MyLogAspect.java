package com.itheima.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class MyLogAspect {
    private static final Logger log = LoggerFactory.getLogger(MyLogAspect.class);
    @Pointcut("@annotation(com.itheima.annotation.MyLog)")
    public void pointCut(){
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();
        log.info("当前时间:{}  MyLog[INFO]\t当前线程{}--类:{}--方法:{}--传入参数:{}", LocalDateTime.now(),Thread.currentThread().getName(), className, methodName, Arrays.toString(args));
        Object proceed = joinPoint.proceed();
        log.info("当前时间:{}  MyLog[INFO]\t当前线程{}--类:{}--方法:{}--结果:{}", LocalDateTime.now(),Thread.currentThread().getName(), className, methodName, proceed.toString());
        return proceed;
    }

    @AfterThrowing("pointCut()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getName();
        log.error("当前时间:{}  MyLog[ERROR]\t当前线程{}--类:{}--方法:{}", LocalDateTime.now(),Thread.currentThread().getName(), className, methodName);
    }
}
