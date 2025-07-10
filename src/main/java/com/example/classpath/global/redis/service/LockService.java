package com.example.classpath.global.redis.service;

import com.example.classpath.global.redis.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LockService {

    private final RedisRepository redisRepository;

    public LockService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public boolean tryLock(String key, String value, long timeoutMillis) {
//        log.info("LOCK ACQUIRED: key={}, value={}", key, value);
//        log.info("UNLOCKING: key={}, value={}", key, value);
        return redisRepository.lock(key, value, timeoutMillis);
    }

    public boolean tryLockWithRetry(String key, String value, long totalTimeoutMillis, int maxAttempts, long intervalMillis) {
        long start = System.currentTimeMillis();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            boolean acquired = tryLock(key, value, totalTimeoutMillis);
            if (acquired) {
                return true;
            }

            if (System.currentTimeMillis() - start > totalTimeoutMillis) {
                break;
            }

            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return false;
    }


    public void releaseLock(String key, String lockValue) {
//        log.info("LOCK ACQUIRED: key={}, value={}", key, lockValue);
//        log.info("UNLOCKING: key={}, value={}", key, lockValue);

        redisRepository.unlock(key, lockValue);
    }
}
