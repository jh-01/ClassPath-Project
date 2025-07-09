package com.example.classpath.domain.enrollmentperiod.service;

import com.example.classpath.domain.enrollmentperiod.dto.request.EnrollmentPeriodRequest;
import com.example.classpath.domain.enrollmentperiod.entity.EnrollmentPeriod;
import com.example.classpath.domain.enrollmentperiod.repository.EnrollmentPeriodRepository;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentPeriodService {

    private final EnrollmentPeriodRepository enrollmentPeriodRepository;

    // 수강신청 기간 설정
    @Transactional
    public void createPeriod(Long id, EnrollmentPeriodRequest request) {
        // TODO: 예외 검증 필요
        EnrollmentPeriod period = EnrollmentPeriod.builder()
                                                  .id(id)
                                                  .startAt(request.getStartAt())
                                                  .endAt(request.getEndAt())
                                                  .build();

        enrollmentPeriodRepository.save(period);
    }

    // 수강신청 기간 수정
    @Transactional
    public void updatePeriod(Long id, EnrollmentPeriodRequest request) {
        EnrollmentPeriod period = enrollmentPeriodRepository.findById(id)
                                                            // TODO: 커스텀 예외로 교체
                                                            .orElseThrow(
                                                                () -> new IllegalArgumentException());
        period.update(request.getStartAt(), request.getEndAt());
    }

    // 날짜 유효성 검사
    private void validatePeriod(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException();
        }
    }

    // 최신 수강신청 기간 조회
    @Transactional(readOnly = true)
    public EnrollmentPeriod getLatestPeriod() {
        return enrollmentPeriodRepository.findTopByOrderByCreatedAtDesc()
            .orElseThrow(()->new NoSuchElementException());
    }
}
