package com.example.classpath.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    DUPLICATE_USERNUMBER(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),

    LECTURE_CODE_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 강의 코드입니다."),
    LECTURE_TIME_INVALID(HttpStatus.BAD_REQUEST,"강의 시작 시간은 종료 시간보다 이전이어야합니다."),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND,"강의가 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}