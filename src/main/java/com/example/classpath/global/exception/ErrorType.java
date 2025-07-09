package com.example.classpath.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    DUPLICATE_USERNUMBER(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    USER_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 조회에 실패했습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),


    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}