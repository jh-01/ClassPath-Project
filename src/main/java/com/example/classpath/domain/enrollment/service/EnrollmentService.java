package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void enroll(Long userId, Long lectureId) {
        try {
            jdbcTemplate.update("CALL enroll_lecture(?, ?)", userId, lectureId); // MySQL에 정의한 저장 프로시저를 호출
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorType.LECTURE_ENROLLMENT_FULL);
        }
    }

    // 수강취소
    @Transactional
    public void cancel(Long userId, Long lectureId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        enrollmentRepository.delete(enrollment);
    }
}