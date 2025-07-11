package com.example.classpath.domain.enrollment.service;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private static final int THREAD_NUM = 100;

    private final ConcurrentLinkedQueue<Long> enrolledUsers = new ConcurrentLinkedQueue<>();
    private List<User> users;
    private Lecture lecture;

    @BeforeEach
    void setup() {
        enrollmentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        lectureRepository.deleteAllInBatch();

        users = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new User(null, "유저" + i, "user" + i, "password", Role.STUDENT))
                .map(userRepository::save)
                .toList();

        lecture = new Lecture("테스트 강의", "C1", 30, DayOfWeek.MON, LocalTime.of(10, 0), LocalTime.of(11, 0));
        lectureRepository.save(lecture);
    }

    @Test
    void 동시성_수강신청_테스트() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_NUM);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_NUM; i++) {
            final long userId = users.get(i).getId();  // 실제 DB에서 부여된 User ID
            executorService.submit(() -> {
                try {
                    barrier.await();  // 동시에 시작
                    enrollmentService.enroll(userId, lecture.getId());
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    e.printStackTrace(); // 예외 출력
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        long endTime = System.currentTimeMillis();

        int actualEnrollment = enrollmentRepository.getEnrollmentCountByLectureId(lecture.getId());

        System.out.println("------ 테스트 결과 ------");
        System.out.println("총 요청 수: " + THREAD_NUM);
        System.out.println("성공 수: " + successCount.get());
        System.out.println("실패 수: " + failureCount.get());
        System.out.println("최종 수강 인원: " + actualEnrollment);
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");
        System.out.println("평균 처리 시간: " + String.format("%.2f", (float)(endTime - startTime)/THREAD_NUM) + "ms/건");

        assertEquals(30, actualEnrollment);
        assertEquals(30, successCount.get());
    }

    @Test
    void 수강취소_동시성_테스트() throws Exception {
        // 1. 수강신청 모두 시도 (100명 중 30명만 성공)
        for (int i = 0; i < users.size(); i++) {
            try {
                enrollmentService.enroll(users.get(i).getId(), lecture.getId());
            } catch (Exception e) {
                // 70명 실패 예상 - 무시
            }
        }

        final int THREAD_COUNT = users.size();  // 100명 전원 시도

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Long> userIdsToCancel = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(userIdsToCancel.size());

        AtomicInteger cancelSuccess = new AtomicInteger();
        AtomicInteger cancelFail = new AtomicInteger();

        long startTime = System.currentTimeMillis();

        for (Long userId : userIdsToCancel) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    enrollmentService.cancel(userId, lecture.getId());
                    cancelSuccess.incrementAndGet();
                } catch (Exception e) {
                    cancelFail.incrementAndGet();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();

        boolean completed = completionLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        if (!completed) {
            throw new RuntimeException("테스트가 10초 이내에 완료되지 않았습니다.");
        }

        int finalEnrollment = enrollmentRepository.getEnrollmentCountByLectureId(lecture.getId());

        long duration = System.currentTimeMillis() - startTime;

        System.out.println("------ 테스트 결과 ------");
        System.out.println("총 시도 횟수: " + THREAD_COUNT);
        System.out.println("수강취소 성공: " + cancelSuccess.get());
        System.out.println("수강취소 실패: " + cancelFail.get());
        System.out.println("최종 수강 인원: " + finalEnrollment);
        System.out.println("총 소요 시간: " + (duration) + "ms");
        System.out.println("평균 처리 시간: " + String.format("%.2f", (float)(duration)/THREAD_NUM) + "ms/건");

        assertEquals(0, finalEnrollment, "모든 수강이 취소되어야 합니다");
        assertEquals(lecture.getMaxEnrollment(), cancelSuccess.get(), "수강취소 성공은 수강인원만큼");
        assertEquals(THREAD_COUNT - lecture.getMaxEnrollment(), cancelFail.get(), "수강하지 않은 인원은 취소 실패");
    }


}