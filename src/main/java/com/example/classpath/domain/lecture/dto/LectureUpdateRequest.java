package com.example.classpath.domain.lecture.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureUpdateRequest {
    private String name;
    @Positive
    private Integer maxEnrollment;
}
