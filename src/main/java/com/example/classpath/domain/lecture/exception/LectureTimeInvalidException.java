package com.example.classpath.domain.lecture.exception;

import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;

public class LectureTimeInvalidException extends BusinessException {
    public LectureTimeInvalidException() {
        super(ErrorType.LECTURE_TIME_INVALID);
    }

    public LectureTimeInvalidException(String message) {
        super(ErrorType.LECTURE_TIME_INVALID, message);
    }
}
