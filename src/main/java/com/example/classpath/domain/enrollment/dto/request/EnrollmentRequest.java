package com.example.classpath.domain.enrollment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EnrollmentRequest {

    @NotNull(message = "강의 ID는 필수입니다.")
    private final Long lectureId;

    public EnrollmentRequest(Long lectureId) {
        this.lectureId = lectureId;
    }
}
