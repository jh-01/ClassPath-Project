package com.example.classpath.global.redis.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisRepository {
    private static final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

    // setIfAbsent() 를 활용해서 SETNX를 실행
    public boolean lock(String key, String value, long timeoutMills) {
        return Boolean.TRUE.equals(redisTemplate
                .opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(timeoutMills)));
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
