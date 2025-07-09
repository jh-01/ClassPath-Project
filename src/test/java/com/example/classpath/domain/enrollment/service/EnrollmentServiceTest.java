package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EnrollmentServiceTest {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private static final int THREAD_NUM = 50;

    @Test
    void 동시성_수강신청_테스트() throws Exception {
        Long userId = 1L;
        Long lectureId = 1L;
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        lectureRepository.save(new Lecture(lectureId, "Test Lecture", "C1", 10, DayOfWeek.MON, startTime, endTime));
        userRepository.save(new User(userId, "테스트 유저", "12345678", "asdf1234!", Role.STUDENT));

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_NUM);
        AtomicInteger successCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);

        for(int i = 0; i < THREAD_NUM; i++){
            executorService.submit(() -> {
                try {
                    barrier.await();
                    enrollmentService.enroll(1L, lectureId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        assertEquals(10, lecture.getCurrentEnrollment());
        assertEquals(10, successCount.get());
    }
}
