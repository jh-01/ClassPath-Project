package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    @Transactional
    public void enroll(Long userId, Long lectureId) {
        // 1. Lecture에 대해 Exclusive Lock 걸기 (트랜잭션 내에서!)
        Lecture lecture = lectureRepository.findByIdForUpdate(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.LECTURE_NOT_FOUND));

        // 2. 비즈니스 로직
        lecture.getCurrentEnrollment();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        lecture.enroll(); // 수강 인원 증가

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .lecture(lecture)
                .build();

        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void cancel(Long userId, Long lectureId) {
        Lecture lecture = lectureRepository.findByIdForUpdate(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.LECTURE_NOT_FOUND));

        // Enrollment 조회하면서 락 걸기
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureIdForUpdate(userId, lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        // 삭제
        enrollmentRepository.delete(enrollment);

        // 수강 인원 감소
        lecture.cancel();
    }
}