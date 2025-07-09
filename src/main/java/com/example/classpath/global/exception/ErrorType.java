package com.example.classpath.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    DUPLICATE_USERNUMBER(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    USER_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "유저 조회에 실패했습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND,"공지사항을 찾을 수 없습니다."),

    // 수강신청
    ALREADY_ENROLLED(HttpStatus.CONFLICT, "이미 수강 신청한 강의입니다."),
    ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "수강 신청 내역을 찾을 수 없습니다."),
    ENROLLMENT_PERIOD_CLOSED(HttpStatus.BAD_REQUEST, "수강신청 기간이 아닙니다."),


    LECTURE_CODE_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 강의 코드입니다."),
    LECTURE_TIME_INVALID(HttpStatus.BAD_REQUEST,"강의 시작 시간은 종료 시간보다 이전이어야합니다."),
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND,"강의가 존재하지 않습니다."),
    LECTURE_ENROLLMENT_FULL(HttpStatus.CONFLICT,"수강 정원이 모두 차서 더 이상 신청할 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}