package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.dto.LectureSearchCondition;
import com.example.classpath.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LectureRepositoryCustom {
    Page<LectureResponse> searchLecture(LectureSearchCondition condition , Pageable pageable);

    List<LectureResponse> findAllUserLecture(User user);

}
