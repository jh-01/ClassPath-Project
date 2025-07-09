package com.example.classpath.domain.lecture.dto;

import com.example.classpath.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StudentResponse {
    private Long id;
    private String userNumber;
    private String name;

    @QueryProjection
    public StudentResponse(User user) {
        this.id = user.getId();
        this.userNumber = user.getUserNumber();
        this.name = user.getName();
    }
}
