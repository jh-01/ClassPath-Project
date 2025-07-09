package com.example.classpath.domain.enrollment.repository;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserIdAndLectureId(Long userId, Long lectureId);

    void deleteByUserIdAndLectureId(Long userId, Long lectureId);

    Optional<Enrollment> findByUserIdAndLectureId(Long userId, Long lectureId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.lecture.id = :lectureId")
    int getEnrollmentCountByLectureId(Long lectureId);
}