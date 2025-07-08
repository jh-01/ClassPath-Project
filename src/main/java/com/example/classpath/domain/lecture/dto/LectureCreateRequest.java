package com.example.classpath.domain.lecture.dto;

import com.example.classpath.domain.lecture.entity.DayOfWeek;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor

public class LectureCreateRequest {
    // TODO 검증 메시지는 추후에 resource 파일로 관리할 예정

    @NotBlank
    private String name;
    @NotBlank
    private String code;

    @Positive
    @NotNull
    private Integer maxEnrollment;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
