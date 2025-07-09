package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnrollmentServiceTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    private static final int THREAD_NUM = 100;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setup() {
        // User 50명 생성
        List<User> users = IntStream.range(0, 50)
                .mapToObj(i -> new User(null, "유저" + i, "user" + i, "password", Role.STUDENT))
                .map(userRepository::save)
                .toList();


        Lecture lecture = new Lecture(1L, "테스트 강의", "C1", 30, DayOfWeek.MON, LocalTime.of(10,0), LocalTime.of(11,0));
        lectureRepository.save(lecture);
    }

    @Test
    void 동시성_수강신청_테스트() throws Exception {
        Lecture lecture = lectureRepository.findByCode("C1").orElseThrow();
        Long lectureId = lecture.getId();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_NUM);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        long startTime = System.currentTimeMillis();

        for (long i = 1; i <= THREAD_NUM; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    barrier.await();  // 동시에 시작
                    enrollmentService.enroll(userId, lecture.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();  // 실패 시 카운트 증가
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        executorService.shutdown();

        int actualEnrollment = enrollmentRepository.getEnrollmentCountByLectureId((lectureId));  // SELECT COUNT(*)

        System.out.println("------ 테스트 결과 ------");
        System.out.println("총 요청 수: " + THREAD_NUM);
        System.out.println("성공 수: " + successCount.get());
        System.out.println("실패 수: " + failureCount.get());
        System.out.println("최종 수강 인원: " + actualEnrollment);
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");

        assertEquals(30, actualEnrollment); // 정원 검사
        assertEquals(30, successCount.get()); // 성공 수 검사
    }
}
