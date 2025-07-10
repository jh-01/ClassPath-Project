package com.example.classpath.domain.enrollment.service;

import com.example.classpath.global.lock.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentFacadeService {
    private final LockService lockService;
    private final EnrollmentService enrollmentService;

    public void enrollWithLock(Long userId, Long lectureId) {
        lockService.executeWithLock(lectureId, () -> enrollmentService.enroll(userId, lectureId));
    }
}