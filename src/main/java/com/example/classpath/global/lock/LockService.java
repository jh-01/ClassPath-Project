package com.example.classpath.global.lock;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;

    public void executeWithLock(Long id, Runnable task) {
        RLock lock = redissonClient.getLock("lock:lecture:" + id);

        boolean acquired = false;
        try {
            // 최대 10초간 락 획득 시도, 락은 획득하면 2초간 유지
            acquired = lock.tryLock(10, 2, TimeUnit.SECONDS);

            if (!acquired) {
                throw new RuntimeException("최대 재시도 횟수 초과");
            }

            System.out.println("락 획득 성공 id=" + id + ", thread=" + Thread.currentThread().getName());
            task.run();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("락 해제 id=" + id + ", thread=" + Thread.currentThread().getName());
            }
        }
    }
}
