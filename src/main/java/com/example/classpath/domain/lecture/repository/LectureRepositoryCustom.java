package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.dto.LectureSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepositoryCustom {
    public Page<LectureResponse> searchLecture(LectureSearchCondition condition , Pageable pageable);
}
