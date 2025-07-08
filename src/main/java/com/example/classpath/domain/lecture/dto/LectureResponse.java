package com.example.classpath.domain.lecture.dto;

import com.example.classpath.domain.lecture.entity.DayOfWeek;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class LectureResponse {
    private Long id;
    private String name;
    private String code;
    private Integer maxEnrollment;
    private Integer currentEnrollment;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.name = lecture.getName();
        this.code = lecture.getCode();
        this.maxEnrollment = lecture.getMaxEnrollment();
        this.currentEnrollment = lecture.getCurrentEnrollment();
        this.dayOfWeek = lecture.getDayOfWeek();
        this.startTime = lecture.getStartTime();
        this.endTime = lecture.getEndTime();
        this.createdAt = lecture.getCreatedAt();
        this.modifiedAt = lecture.getModifiedAt();
    }
}
