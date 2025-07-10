package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnrollmentServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentFacadeService enrollmentFacadeService;

    private static final int STUDENT_COUNT = 100;  // 100명 동시 시도
    private static final int MAX_ENROLLMENT = 30; // 정원 30명

    private Lecture lecture;

    @BeforeEach
    void setUp() {
        // 사용자 1000명 생성
        for (int i = 0; i < STUDENT_COUNT; i++) {
            User user = new User(null, "user" + i, "2025" + i, "!1Password", Role.STUDENT);
            userRepository.save(user);
        }

        // 정원 30명의 강의 생성
        lecture = Lecture.of("Spring boot", "1234", MAX_ENROLLMENT, DayOfWeek.MON, LocalTime.of(10, 0), LocalTime.of(11, 0));
        lectureRepository.save(lecture);
    }

    @Test
    void 동시_수강신청_테스트() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(STUDENT_COUNT);
        CyclicBarrier barrier = new CyclicBarrier(STUDENT_COUNT);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        List<User> users = userRepository.findAll();

        long start = System.currentTimeMillis();

        for (User user : users) {
            executorService.execute(() -> {
                try {
                    barrier.await(); // 동시에 시작
                    enrollmentFacadeService.enrollWithLock(user.getId(), lecture.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet(); // 실패 처리
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long end = System.currentTimeMillis();
        long duration = end - start;

        long enrolledCount = enrollmentRepository.countByLectureId(lecture.getId());

        System.out.println("최종 수강 인원: " + enrolledCount);
        System.out.println("성공한 수강 신청 수: " + successCount.get());
        System.out.println("실패한 수강 신청 수: " + failureCount.get());
        System.out.println("걸린 시간: " + duration + "ms");

        assertEquals(MAX_ENROLLMENT, enrolledCount);
        assertEquals(MAX_ENROLLMENT, successCount.get());
    }
}