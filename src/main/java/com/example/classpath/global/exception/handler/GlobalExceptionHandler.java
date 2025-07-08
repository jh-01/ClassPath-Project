package com.example.classpath.global.exception.handler;

import com.example.classpath.global.common.ApiResponse;
import com.example.classpath.global.common.ErrorBody;
import com.example.classpath.global.common.ErrorDetail;
import com.example.classpath.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(ApiResponse.error(e.getErrorType()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorBody>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorDetail> errors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ErrorDetail errorDetail = ErrorDetail.builder()
                    .field(fieldError.getField())
                    .message(messageSource.getMessage(Objects.requireNonNull(fieldError), Locale.KOREA))
                    .build();
            errors.add(errorDetail);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.validationError("요청값이 올바르지 않습니다.", new ErrorBody(errors)));
    }

}
