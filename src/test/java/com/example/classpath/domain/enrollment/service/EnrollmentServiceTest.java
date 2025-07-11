package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnrollmentServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    private Long lectureId;
    private final int threadCount = 100;

    @BeforeEach
    void setup() {
        // User 100명 생성
        List<User> users = IntStream.range(0, 100)
                .mapToObj(i -> new User(null, "유저" + i, "user" + i, "password", Role.STUDENT))
                .map(userRepository::save)
                .toList();


        Lecture lecture = new Lecture("Spring", "12345", 30, DayOfWeek.MON, LocalTime.of(10,0), LocalTime.of(11,0));
        lectureRepository.save(lecture);
        lectureId = lecture.getId();
    }

    @Test
    void 동시성_수강신청_테스트() throws InterruptedException {

        // given
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);

        long start = System.currentTimeMillis();

        // when
        for (long userId = 1; userId <= threadCount; userId++) {
            final long uid = userId;
            executorService.submit(() -> {
                try {
                    enrollmentService.enroll(uid, lectureId);
                    success.incrementAndGet();
                } catch (Exception e) {
                    failure.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 종료 대기
        executorService.shutdown();

        long duration = System.currentTimeMillis() - start;

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM enrollment WHERE lecture_id = ?",
                Integer.class,
                lectureId
        );

        System.out.println("최종 수강 인원: " + count);
        System.out.println("성공한 수강 신청 수: " + success.get());
        System.out.println("실패한 수강 신청 수: " + failure.get());
        System.out.println("걸린 시간: " + duration + "ms");

        assertThat(count).isEqualTo(30);
        assertThat(success.get()).isEqualTo(30);
        assertThat(failure.get()).isEqualTo(70);
    }
}