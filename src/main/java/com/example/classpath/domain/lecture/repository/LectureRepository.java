package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Long>, LectureRepositoryCustom {
    boolean existsByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 잠금의 Exclusive Lock 설정
    @Query("select l from Lecture l where l.id = :lectureId")
    Optional<Lecture> findByIdForUpdate(Long lectureId);
}
