package com.example.classpath.global.exception;

import com.example.classpath.global.common.ApiResponse;
import com.example.classpath.global.common.ErrorBody;
import com.example.classpath.global.common.ErrorDetail;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleException(CustomException customException) {
        ApiResponse<?> response = ApiResponse.error(customException.getErrorType());
        return ResponseEntity
                .status(customException.getErrorType().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorBody>> handleValidation(MethodArgumentNotValidException e) {
        List<ErrorDetail> errorDetails = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorBody errorBody = new ErrorBody(errorDetails);

        return ResponseEntity.badRequest()
                .body(ApiResponse.validationError("입력값이 유효하지 않습니다.", errorBody));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<ErrorBody>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorDetail> errorDetails = ex.getConstraintViolations().stream()
                .map(violation -> {
                    String fieldPath = violation.getPropertyPath().toString();
                    String field = fieldPath.substring(fieldPath.lastIndexOf('.') + 1);
                    return new ErrorDetail(field, violation.getMessage());
                })
                .collect(Collectors.toList());

        ErrorBody errorBody = new ErrorBody(errorDetails);

        return ResponseEntity.badRequest()
                .body(ApiResponse.validationError("제약 조건 위반", errorBody));
    }


}
