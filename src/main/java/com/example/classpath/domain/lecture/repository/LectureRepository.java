package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Long>, LectureRepositoryCustom {
    boolean existsByCode(String code);

    @Query(value = "SELECT * FROM lecture WHERE id = :id FOR UPDATE", nativeQuery = true)
    Optional<Lecture> findByIdForUpdate(@Param("id") Long id);
}
