package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Long>, LectureRepositoryCustom {
    boolean existsByCode(String code);

    Optional<Lecture> findByCode(String c1);
}
