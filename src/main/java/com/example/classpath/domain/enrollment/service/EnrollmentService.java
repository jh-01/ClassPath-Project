package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public void enrollWithRetry(Long userId, Long lectureId) {
        int retries = 5;
        while (retries > 0) {
            try {
                enroll(userId, lectureId);
                break;
            } catch (OptimisticLockException e) {
                retries--;
                if (retries == 0) throw e;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // 수강신청
    @Transactional
    public void enroll(Long userId, Long lectureId) {
        if (enrollmentRepository.existsByUserIdAndLectureId(userId, lectureId)) {
            throw new BusinessException(ErrorType.ALREADY_ENROLLED);
        }

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Lecture lecture = lectureRepository.findByIdForUpdate(lectureId)
                                           .orElseThrow(() -> new BusinessException(ErrorType.LECTURE_NOT_FOUND));

        lecture.enroll(); // 정원 초과 시 예외 발생 처리

        Enrollment enrollment = Enrollment.builder()
                                          .user(user)
                                          .lecture(lecture)
                                          .build();
        enrollmentRepository.save(enrollment);
    }

    // 수강취소
    @Transactional
    public void cancel(Long userId, Long lectureId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                                                    .orElseThrow(() -> new BusinessException(ErrorType.ENROLLMENT_NOT_FOUND));

        enrollmentRepository.delete(enrollment);
    }
}