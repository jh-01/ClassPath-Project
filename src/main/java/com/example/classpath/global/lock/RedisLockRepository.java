package com.example.classpath.global.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    // Lock key 설정
    private static final String LOCK = "lock:lecture:";

    // Lock 시간 설정 (3초)
    private static final Duration EXPIRE_TIME = Duration.ofMillis(2000);

    private final RedisTemplate<String, String> redisTemplate;

    // 락 걸 때 사용할 고유값을 저장, 해제 시 락 소유자인지 검증
    private final ThreadLocal<String> lockValue = new ThreadLocal<>();

    public Boolean lock(Long key) {
        String redisKey = LOCK + key;
        String value = UUID.randomUUID().toString(); // 락 소유자가를 구분하기 위한 UUID 생성
        lockValue.set(value); // value를 현재 스레드에 저장
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, value, EXPIRE_TIME); // 락 실행
        return Boolean.TRUE.equals(result);
    }

    public Boolean unlock(Object key) {
        String redisKey = LOCK + key;
        String value = lockValue.get(); // 현재 스레드가 가진 UUID 조회
        lockValue.remove(); // UUID 제거

        // 저장된 UUID와 같으면 락 해제
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<Long> script = RedisScript.of(luaScript, Long.class);
        // Lua 스크립트 실행
        Long result = redisTemplate.execute(script, Collections.singletonList(redisKey), value);
        return result > 0;

    }
}
