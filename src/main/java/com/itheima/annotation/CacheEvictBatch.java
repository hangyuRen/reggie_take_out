package com.itheima.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvictBatch {
    String value() default "";
    String[] keys() default {};
}
