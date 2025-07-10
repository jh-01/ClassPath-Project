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
        // 1. 먼저 수강신청을 모두 성공시키는 작업 (최대 수강인원만큼)
        List<Long> enrolledUsersList = new ArrayList<>();  // 임시 리스트 생성
        for (int i = 0; i < lecture.getMaxEnrollment(); i++) {
            final long userId = users.get(i).getId();
            enrollmentService.enroll(userId, lecture.getId());
            enrolledUsersList.add(userId);
            enrolledUsers.add(userId);
        }

        final int TOTAL_ATTEMPTS = 100;  // 총 100번 시도
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_ATTEMPTS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(TOTAL_ATTEMPTS);

        AtomicInteger cancelSuccess = new AtomicInteger();
        AtomicInteger cancelFail = new AtomicInteger();

        // 30명의 userId를 100번 반복해서 사용
        List<Long> userIdsToCancel = new ArrayList<>();
        for (int i = 0; i < TOTAL_ATTEMPTS; i++) {
            userIdsToCancel.add(enrolledUsersList.get(i % lecture.getMaxEnrollment()));
        }

        // 시작 시간 측정
        long startTime = System.currentTimeMillis();
        
        for (Long userId : userIdsToCancel) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    enrollmentService.cancel(userId, lecture.getId());
                    cancelSuccess.incrementAndGet();
                } catch (Exception e) {
                    cancelFail.incrementAndGet();
                    System.err.println("Cancel failed for userId: " + userId + " - " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        
        boolean completed = completionLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        // 종료 시간 측정
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        if (!completed) {
            throw new RuntimeException("테스트가 30초 이내에 완료되지 않았습니다.");
        }

        // 잠시 대기 후 최종 결과 확인
        Thread.sleep(100);
        
        int finalEnrollment = enrollmentRepository.getEnrollmentCountByLectureId(lecture.getId());
        
        System.out.println("------ 테스트 결과 ------");
        System.out.println("총 시도 횟수: " + TOTAL_ATTEMPTS);
        System.out.println("수강취소 성공: " + cancelSuccess.get());
        System.out.println("수강취소 실패: " + cancelFail.get());
        System.out.println("최종 수강 인원: " + finalEnrollment);
        System.out.println("총 소요 시간: " + duration + "ms");
        System.out.println("평균 처리 시간: " + String.format("%.2f", (float)duration/TOTAL_ATTEMPTS) + "ms/건");
        
        assertEquals(0, finalEnrollment, "모든 수강이 취소되어야 합니다");
        assertEquals(lecture.getMaxEnrollment(), cancelSuccess.get(), "정확히 30개의 수강취소만 성공해야 합니다");
        assertEquals(TOTAL_ATTEMPTS - lecture.getMaxEnrollment(), cancelFail.get(), "70개의 수강취소는 실패해야 합니다");
    }
}