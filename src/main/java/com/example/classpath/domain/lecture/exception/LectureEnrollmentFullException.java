package com.example.classpath.domain.lecture.exception;

import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;

public class LectureEnrollmentFullException extends BusinessException {
    public LectureEnrollmentFullException() {
        super(ErrorType.LECTURE_ENROLLMENT_FULL);
    }

    public LectureEnrollmentFullException(String message) {
        super(ErrorType.LECTURE_ENROLLMENT_FULL, message);
    }
}
