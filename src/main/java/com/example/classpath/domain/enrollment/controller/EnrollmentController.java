package com.example.classpath.domain.enrollment.controller;

import com.example.classpath.domain.enrollment.dto.request.EnrollmentRequest;
import com.example.classpath.domain.enrollment.service.EnrollmentFacadeService;
import com.example.classpath.domain.enrollment.service.EnrollmentService;
import com.example.classpath.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final EnrollmentFacadeService enrollmentFacadeService;

    // 수강 신청
    @PostMapping
    public ApiResponse<Void> enroll(@RequestBody EnrollmentRequest request) {
        enrollmentFacadeService.enrollWithLock(request.getUserId(), request.getLectureId());
        return ApiResponse.success("수강 신청되었습니다.", null);
    }

    // 수강 취소
    @DeleteMapping("/{lectureId}")
    public ApiResponse<Void> cancel(@PathVariable Long lectureId, @RequestParam Long userId) {
        enrollmentService.cancel(userId, lectureId);
        return ApiResponse.success("수강 취소되었습니다.", null);
    }
}
