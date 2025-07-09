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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    // 수강신청
    @Transactional
    public void enroll(Long userId, Long lectureId) {
        if (enrollmentRepository.existsByUserIdAndLectureId(userId, lectureId)) {
            throw new BusinessException(ErrorType.ALREADY_ENROLLED);
        }

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        // 락 걸고 가져오기
        Lecture lecture = lectureRepository.findByIdForUpdate(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의 없음"));

        lecture.enroll();

        Enrollment enrollment = Enrollment.builder()
                                          .user(user)
                                          .lecture(lecture)
                                          .build();
        enrollmentRepository.save(enrollment);
        lectureRepository.save(lecture);
    }

    // 수강취소
    @Transactional
    public void cancel(Long userId, Long lectureId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                                                    .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        enrollmentRepository.delete(enrollment);
    }
}