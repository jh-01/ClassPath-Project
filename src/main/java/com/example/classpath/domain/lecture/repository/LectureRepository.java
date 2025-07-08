package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    boolean existsByCode(String code);
}
