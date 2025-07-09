package com.example.classpath.domain.lecture.exception;

import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;

public class LectureCodeAlreadyExistException extends BusinessException {
    public LectureCodeAlreadyExistException() {
        super(ErrorType.LECTURE_CODE_ALREADY_EXIST);
    }

    public LectureCodeAlreadyExistException(String message) {
        super(ErrorType.LECTURE_CODE_ALREADY_EXIST, message);
    }
}
