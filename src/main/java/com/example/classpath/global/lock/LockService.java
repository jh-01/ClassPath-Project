package com.example.classpath.global.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockService {

    private final RedisLockRepository redisLockRepository;

    public void executeWithLock(Long id, Runnable task) {
        int retryCount = 0;

        // Lock 획득 시도
        while (!redisLockRepository.lock(id)) {
            retryCount++;
            // 30번까지 시도 가능, 횟수 초과시 예외 발생
            if (retryCount >= 10) {
                throw new RuntimeException("최대 재시도 횟수 초과");
            }
            try {
                //SpinLock 방식이 redis 에게 주는 부하를 줄여주기위한 sleep
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // lock 획득 성공시
        try {
            System.out.println("락 획득 성공 id=" + id + ", thread=" + Thread.currentThread().getName());
            task.run();
        } finally {
            // 완료 후 락 해제
            redisLockRepository.unlock(id);
            System.out.println("락 해제 id=" + id + ", thread=" + Thread.currentThread().getName());
        }
    }
}

