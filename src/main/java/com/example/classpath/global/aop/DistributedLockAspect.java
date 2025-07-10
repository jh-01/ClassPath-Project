package com.example.classpath.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import java.util.Collections;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {
    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(distributedLock)")
    public Object executeLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = generateKey(joinPoint, distributedLock.key());
        String value = UUID.randomUUID().toString();
        
        try {
            // 락 획득 시도
            boolean acquired = acquireLockWithRetry(
                key, 
                value, 
                distributedLock.timeoutMs(),
                distributedLock.maxAttempts(),
                distributedLock.intervalMs()
            );

            if (!acquired) {
                throw new RuntimeException("락 획득 실패");
            }

            // 메소드 실행
            return joinPoint.proceed();
            
        } finally {
            // 락 해제
            releaseLock(key, value);
        }
    }

    private boolean acquireLockWithRetry(String key, String value, long timeoutMs, int maxAttempts, long intervalMs) {
        long startTime = System.currentTimeMillis();
        
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            boolean acquired = Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(key, value, Duration.ofMillis(timeoutMs)));
            
            if (acquired) {
                return true;
            }

            // 전체 타임아웃 체크
            if (System.currentTimeMillis() - startTime >= timeoutMs) {
                return false;
            }

            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false;
    }

    private void releaseLock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                       "return redis.call('del', KEYS[1]) " +
                       "else return 0 end";
        
        redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            Collections.singletonList(key),
            value
        );
    }

    private String generateKey(ProceedingJoinPoint joinPoint, String keyTemplate) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        
        EvaluationContext context = new StandardEvaluationContext();
        
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        
        ExpressionParser parser = new SpelExpressionParser();
        String resolvedKey = parser.parseExpression(keyTemplate).getValue(context, String.class);
        
        return String.format("lock:%s:%s", methodName, resolvedKey);
    }
}