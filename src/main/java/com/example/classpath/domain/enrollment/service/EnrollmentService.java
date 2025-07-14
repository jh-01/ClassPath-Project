package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.aop.DistributedLock;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import com.example.classpath.global.redis.service.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final LockService lockService;

    @Transactional
    @DistributedLock(
            key = "#lectureId",
            timeoutMs = 3000,
            maxAttempts = 5,
            intervalMs = 50
    )

    public void enroll(Long userId, Long lectureId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.LECTURE_NOT_FOUND));

        // 수강 인원 체크
        lecture.enroll();

        lectureRepository.save(lecture);

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .lecture(lecture)
                .build();

        enrollmentRepository.save(enrollment);
    }

    // 수강취소
    @Transactional
    @DistributedLock(
            key = "#lectureId",
            timeoutMs = 3000,
            maxAttempts = 5,
            intervalMs = 50
    )
    public void cancel(Long userId, Long lectureId) {
        // 락을 획득한 후에도 존재 여부를 한번 더 확인
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        Lecture lecture = enrollment.getLecture();
        lecture.cancel();

        // 명시적인 영속성 컨텍스트 플러시
        enrollmentRepository.delete(enrollment);
        enrollmentRepository.flush();
    }
}