package com.example.classpath.domain.enrollmentperiod.service;

import com.example.classpath.domain.enrollmentperiod.dto.request.EnrollmentPeriodRequest;
import com.example.classpath.domain.enrollmentperiod.entity.EnrollmentPeriod;
import com.example.classpath.domain.enrollmentperiod.repository.EnrollmentPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentPeriodService {

    private final EnrollmentPeriodRepository enrollmentPeriodRepository;

    // 수강신청 기간 등록
    @Transactional
    public void enroll(Long id, EnrollmentPeriodRequest request) {
        // TODO: 예외 검증 필요
        EnrollmentPeriod period = EnrollmentPeriod.builder()
            .id(id)
            .startAt(request.getStartAt())
            .endAt(request.getEndAt())
            .build();

        enrollmentPeriodRepository.save(period);
    }
}
