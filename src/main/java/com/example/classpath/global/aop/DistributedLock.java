package com.example.classpath.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key();
    long timeoutMs() default 3000L;    // 전체 타임아웃
    int maxAttempts() default 5;       // 최대 시도 횟수
    long intervalMs() default 50L;      // 재시도 간격
}