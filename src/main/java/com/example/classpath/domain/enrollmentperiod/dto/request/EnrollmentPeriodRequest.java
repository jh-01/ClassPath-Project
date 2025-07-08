package com.example.classpath.domain.enrollmentperiod.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EnrollmentPeriodRequest {

    @NotNull(message = "시작일은 필수입니다.")
    private final LocalDateTime startAt;

    @NotNull(message = "종료일은 필수입니다.")
    private final LocalDateTime endAt;

    public EnrollmentPeriodRequest(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
