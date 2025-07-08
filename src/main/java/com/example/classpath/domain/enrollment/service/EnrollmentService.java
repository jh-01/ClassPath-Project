package com.example.classpath.domain.enrollment.service;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.enrollment.repository.EnrollmentRepository;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
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
    public void enroll(Long userId, Long lectureId) {
        if (enrollmentRepository.existsByUserIdAndLectureId(userId, lectureId)) {
            throw new IllegalArgumentException();
        }

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> {
                                      // TODO: 커스텀 예외로 교체
                                      throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
                                  });

        Lecture lecture = lectureRepository.findById(lectureId)
                                           .orElseThrow(() -> {
                                               // TODO: 커스텀 예외로 교체
                                               throw new IllegalArgumentException(
                                                   "해당 강의가 존재하지 않습니다.");
                                           });

        Enrollment enrollment = Enrollment.builder()
                                          .user(user)
                                          .lecture(lecture)
                                          .build();
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void cancel(Long userId, Long lectureId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndLectureId(userId, lectureId)
                                                    .orElseThrow(() -> {
                                                        // TODO: 커스텀 예외로 교체
                                                        throw new IllegalArgumentException();
                                                    });

        enrollmentRepository.delete(enrollment);
    }
}