package com.example.classpath.domain.enrollment.repository;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserIdAndLectureId(Long userId, Long lectureId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.lecture.id = :lectureId")
    int getEnrollmentCountByLectureId(@Param("lectureId") Long lectureId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Enrollment e where e.user.id = :userId and e.lecture.id = :lectureId")
    Optional<Enrollment> findByUserIdAndLectureIdForUpdate(@Param("userId") Long userId, @Param("lectureId") Long lectureId);
}