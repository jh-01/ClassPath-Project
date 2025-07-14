package com.example.classpath.domain.notice.service;

import com.example.classpath.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ViewCountService {
    private final RedisTemplate<String, String> redisTemplate;
    private final NoticeRepository noticeRepository;

    private static final String USER_VIEW_KEY = "notice:view:";         // 유저별 조회 기록
    private static final String NOTICE_VIEW_COUNT_KEY = "notice:count:"; // 공지별 조회수
    private static final String NOTICE_RANKING_KEY = "notice:ranking";   // 인기 공지 랭킹

    // 조회수 증가
    public boolean increaseViewCount(Long noticeId, Long userId) {
        String key = USER_VIEW_KEY + noticeId + ":" + userId;
        Boolean isViewed = redisTemplate.hasKey(key);

        /**
         * set(key, value, timeout, timeUnit) : 만료 시간을 지정하여 값 저장
         * increment(key) : key에 저장된 값을 +1 증가
         * incrementScore(key, member, delta) : key 멤버 점수에 1을 증가
         */
        if (isViewed != null && !isViewed) {
            redisTemplate.opsForValue().set(key, "1", 24, TimeUnit.HOURS); // 24시간 후 자동 만료(어뷰징 방지)
            redisTemplate.opsForValue().increment(NOTICE_VIEW_COUNT_KEY + noticeId); // 조회수 +1

            // ZSet 점수 증가 (랭킹) ex) noticeId가 1이라면 noticeId가 1인 공지사항의 점수를 1 올린다.
            redisTemplate.opsForZSet().incrementScore(NOTICE_RANKING_KEY, noticeId.toString(), 1);
            return true;
        }

        return false;
    }

    /**
     * opsForValue().get(key) : key에 저장된 문자열을 가져오는 것
     */
    // Redis에 있는 조회수 가져오기
    public Long getViewCount(Long noticeId) {
        String count = redisTemplate.opsForValue().get(NOTICE_VIEW_COUNT_KEY + noticeId);
        return count != null ? Long.parseLong(count) : 0L; // Redis에 저장된 조회수가 없으면 0을 반환
    }

    // 3분 마다 조회수 반영
    @Scheduled(cron = "0 0/3 * * * ?")
    public void applyViewCountToRDB() {
        Set<String> keys = redisTemplate.keys(NOTICE_VIEW_COUNT_KEY + "*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            String noticeIdStr = key.replace(NOTICE_VIEW_COUNT_KEY, "");
            Long noticeId = Long.parseLong(noticeIdStr);

            String countStr = redisTemplate.opsForValue().get(key); // redis에 저장된 조회수를 String 으로 가져옴
            if (countStr != null && !countStr.isEmpty()) {
                Long count = Long.parseLong(countStr); // Long 타입으로 변경

                // DB 반영
                noticeRepository.increaseViewCount(noticeId, count);
            }
        }
    }

    // 매일 자정 조회수 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetViewCountToRDB() {
        Set<String> keys = redisTemplate.keys(NOTICE_VIEW_COUNT_KEY + "*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys); // 조회수 키가 존재하면 키를 삭제하여 조회수 초기화
        }
    }

    /**
     * TypedTuple<String> : ZSet 원소(멤버 + 점수)
     * Set : 중복 없이 모아둔 집합
     * reverseRangeWithScores(key, start, end) : 점수 내림차순으로 멤버와 점수 조회
     */
    // Top10 공지사항 Id 추출
    public List<Long> getRankingNoticeIds() {
        Set<ZSetOperations.TypedTuple<String>> rankedSet =
                redisTemplate.opsForZSet().reverseRangeWithScores(NOTICE_RANKING_KEY, 0, 9);

        List<Long> result = new ArrayList<>();
        if (rankedSet != null) {
            for (ZSetOperations.TypedTuple<String> tuple : rankedSet) {
                result.add(Long.parseLong(tuple.getValue())); // 공지 ID를 Long 타입으로 변환 후 List에 저장
            }
        }

        return result;
    }
}