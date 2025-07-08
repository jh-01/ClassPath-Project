package com.example.classpath.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    DUPLICATE_USERNUMBER(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),

    // 강의
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 강의를 찾을 수 없습니다."),

    // 수강신청
    ALREADY_ENROLLED(HttpStatus.CONFLICT, "이미 수강 신청한 강의입니다."),
    ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "수강 신청 내역을 찾을 수 없습니다."),
    ENROLLMENT_PERIOD_CLOSED(HttpStatus.BAD_REQUEST, "수강신청 기간이 아닙니다."),


    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}