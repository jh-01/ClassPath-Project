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
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
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
    public void enroll(Long userId, Long lectureId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Lecture lecture = lectureRepository.findByIdWithPessimisticLock(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.LECTURE_NOT_FOUND));

        // 수강 인원 체크
        int enrolledCount = lecture.getCurrentEnrollment();

        // 수강 인원 증가
        lecture.enroll();

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .lecture(lecture)
                .build();

        enrollmentRepository.save(enrollment);
    }

    // 수강취소
    @Transactional
    public void cancel(Long userId, Long lectureId) {
        // 락을 획득한 후에도 존재 여부를 한번 더 확인
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        // 명시적인 영속성 컨텍스트 플러시
        enrollmentRepository.delete(enrollment);
        enrollmentRepository.flush();
    }
}