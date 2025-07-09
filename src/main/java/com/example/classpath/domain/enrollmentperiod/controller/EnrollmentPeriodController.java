package com.example.classpath.domain.enrollmentperiod.controller;

import com.example.classpath.domain.enrollmentperiod.dto.request.EnrollmentPeriodRequest;
import com.example.classpath.domain.enrollmentperiod.entity.EnrollmentPeriod;
import com.example.classpath.domain.enrollmentperiod.service.EnrollmentPeriodService;
import com.example.classpath.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment-period")
public class EnrollmentPeriodController {
    private final EnrollmentPeriodService enrollmentPeriodService;

    // 수강신청 기간 설정
    @PostMapping("/{id}")
    public ApiResponse<Void> createPeriod(@PathVariable Long id, @RequestBody EnrollmentPeriodRequest request) {
        enrollmentPeriodService.createPeriod(id, request);
        return ApiResponse.success("수강신청 기간이 설정되었습니다.", null);
    }

    // 수강신청 기간 수정
    @PatchMapping("/{id}")
    public ApiResponse<Void> updatePeriod(@PathVariable Long id, @RequestBody EnrollmentPeriodRequest request) {
        enrollmentPeriodService.updatePeriod(id, request);
        return ApiResponse.success("수강신청 기간이 수정되었습니다.", null);
    }

    // 수강신청 기간 조회
    @GetMapping("/latest")
    public ApiResponse<EnrollmentPeriod> getLatestPeriod() {
        EnrollmentPeriod enrollmentPeriod = enrollmentPeriodService.getLatestPeriod();
        return ApiResponse.success("최신 수강신청 기간을 조회했습니다.", enrollmentPeriod);
    }
}
