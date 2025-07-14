package com.example.classpath.domain.enrollmentperiod.service;

import com.example.classpath.domain.enrollmentperiod.dto.request.EnrollmentPeriodRequest;
import com.example.classpath.domain.enrollmentperiod.entity.EnrollmentPeriod;
import com.example.classpath.domain.enrollmentperiod.repository.EnrollmentPeriodRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentPeriodService {

    private final EnrollmentPeriodRepository enrollmentPeriodRepository;

    // 수강신청 기간 설정
    @Transactional
    public void createPeriod(EnrollmentPeriodRequest request) {
        validatePeriod(request.getStartAt(), request.getEndAt());

        if (enrollmentPeriodRepository.existsBy()) {
            throw new BusinessException(ErrorType.ENROLLMENT_PERIOD_ALREADY_EXISTS);
        }

        EnrollmentPeriod period = EnrollmentPeriod.builder()
                                                  .startAt(request.getStartAt())
                                                  .endAt(request.getEndAt())
                                                  .build();

        enrollmentPeriodRepository.save(period);
    }

    // 수강신청 기간 수정
    @Transactional
    public void updatePeriod(Long id, EnrollmentPeriodRequest request) {
        validatePeriod(request.getStartAt(), request.getEndAt());
        EnrollmentPeriod period = enrollmentPeriodRepository.findById(id)
                                                            .orElseThrow(() -> new BusinessException(
                                                                ErrorType.ENROLLMENT_PERIOD_NOT_FOUND));
        period.update(request.getStartAt(), request.getEndAt());
    }

    // 날짜 유효성 검사
    private void validatePeriod(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.isAfter(endAt)) {
            throw new BusinessException(ErrorType.INVALID_ENROLLMENT_PERIOD);
        }
    }

    // 최신 수강신청 기간 조회
    @Transactional(readOnly = true)
    public EnrollmentPeriod getPeriod() {
        return enrollmentPeriodRepository.findFirstBy()
            .orElseThrow(()->new BusinessException(ErrorType.ENROLLMENT_PERIOD_NOT_FOUND));
    }
}
