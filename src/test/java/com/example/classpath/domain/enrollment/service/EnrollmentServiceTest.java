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

        assertEquals(30, actualEnrollment);
        assertEquals(30, successCount.get());
    }

    @Test
    void 수강취소_동시성_테스트() throws Exception {
        // 1. 먼저 수강신청을 모두 성공시키는 작업 (최대 수강인원만큼)
        for (int i = 0; i < lecture.getMaxEnrollment(); i++) {
            final long userId = users.get(i).getId();
            enrollmentService.enroll(userId, lecture.getId());
            enrolledUsers.add(userId);
        }

        int cancelThreadNum = lecture.getMaxEnrollment();  // 30개로 수정
        ExecutorService executor = Executors.newFixedThreadPool(cancelThreadNum);
        CountDownLatch startLatch = new CountDownLatch(1); // 시작 신호용
        CountDownLatch completionLatch = new CountDownLatch(cancelThreadNum); // 완료 대기용

        AtomicInteger cancelSuccess = new AtomicInteger();
        AtomicInteger cancelFail = new AtomicInteger();

        // 모든 userId를 미리 리스트로 준비
        List<Long> userIdsToCancel = new ArrayList<>(enrolledUsers);
        
        // 모든 작업을 준비
        for (Long userId : userIdsToCancel) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 시작 신호를 기다림
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

        // 모든 스레드 동시 시작
        startLatch.countDown();
        
        // 완료 대기 (30초 타임아웃 설정)
        boolean completed = completionLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        if (!completed) {
            throw new RuntimeException("테스트가 30초 이내에 완료되지 않았습니다.");
        }

        int finalEnrollment = enrollmentRepository.getEnrollmentCountByLectureId(lecture.getId());
        
        System.out.println("수강취소 성공: " + cancelSuccess.get());
        System.out.println("수강취소 실패: " + cancelFail.get());
        System.out.println("최종 수강 인원: " + finalEnrollment);
        
        assertEquals(0, finalEnrollment, "모든 수강신청이 취소되어야 합니다");
        assertEquals(cancelThreadNum, cancelSuccess.get(), "모든 취소 요청이 성공해야 합니다");
    }


//    @Test
//    void 수강신청_및_취소_동시성_테스트() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
//        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
//        CyclicBarrier barrier = new CyclicBarrier(THREAD_NUM);
//
//        AtomicInteger enrollSuccess = new AtomicInteger();
//        AtomicInteger enrollFail = new AtomicInteger();
//        AtomicInteger cancelSuccess = new AtomicInteger();
//        AtomicInteger cancelFail = new AtomicInteger();
//
//        ConcurrentLinkedQueue<Long> enrolledUsers = new ConcurrentLinkedQueue<>();
//
//        for (int i = 0; i < THREAD_NUM; i++) {
//            final long userId = users.get(i).getId();
//            executorService.submit(() -> {
//                try {
//                    barrier.await();
//                    if (ThreadLocalRandom.current().nextBoolean()) {
//                        enrollmentService.enroll(userId, lecture.getId());
//                        enrolledUsers.offer(userId);
//                        enrollSuccess.incrementAndGet();
//                        System.out.println("Enroll success userId: " + userId);
//                    } else {
//                        Long cancelUserId = enrolledUsers.poll();
//                        System.out.println("Cancel attempt userId: " + cancelUserId);
//                        if (cancelUserId != null) {
//                            enrollmentService.cancel(cancelUserId, lecture.getId());
//                            cancelSuccess.incrementAndGet();
//                        } else {
//                            // 취소 시도할 등록된 유저가 없으므로 실패 처리
//                            cancelFail.incrementAndGet();
//                        }
//                    }
//                } catch (Exception e) {
//                    if (e instanceof BusinessException) {
//                        // 수강 신청/취소 실패 처리
//                        if (e.getMessage().contains("ALREADY_ENROLLED") || e.getMessage().contains("LECTURE_ENROLLMENT_FULL")) {
//                            enrollFail.incrementAndGet();
//                        } else if (e.getMessage().contains("ENROLLMENT_NOT_FOUND")) {
//                            cancelFail.incrementAndGet();
//                        }
//                    } else {
//                        enrollFail.incrementAndGet();
//                        cancelFail.incrementAndGet();
//                    }
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        executorService.shutdown();
//        executorService.awaitTermination(30, TimeUnit.SECONDS);
//
//        int finalCount = enrollmentRepository.getEnrollmentCountByLectureId(lecture.getId());
//        System.out.println("수강신청 성공: " + enrollSuccess.get());
//        System.out.println("수강신청 실패: " + enrollFail.get());
//        System.out.println("수강취소 성공: " + cancelSuccess.get());
//        System.out.println("수강취소 실패: " + cancelFail.get());
//        System.out.println("최종 수강 인원: " + finalCount);
//    }


}