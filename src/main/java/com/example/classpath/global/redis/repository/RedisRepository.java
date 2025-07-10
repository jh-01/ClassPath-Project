package com.example.classpath.global.redis.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // setIfAbsent() 를 활용해서 SETNX를 실행
    public boolean lock(String key, String value, long timeoutMillis) {
        // Redis에 고유한 value 저장
        return Boolean.TRUE.equals(redisTemplate
                .opsForValue()
                .setIfAbsent(key, value, Duration.ofMillis(timeoutMillis)));
    }

    public void unlock(String key, String lockValue) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if (lockValue.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
}
