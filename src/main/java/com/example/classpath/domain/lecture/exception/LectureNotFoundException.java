package com.example.classpath.domain.lecture.exception;

import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;

public class LectureNotFoundException extends BusinessException {
    public LectureNotFoundException() {
        super(ErrorType.LECTURE_NOT_FOUND);
    }

    public LectureNotFoundException(String message) {
        super(ErrorType.LECTURE_NOT_FOUND, message);
    }
}
