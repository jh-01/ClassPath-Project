package com.example.classpath.global.common;

import com.example.classpath.global.exception.ErrorType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, @Nullable T data){
        return new ApiResponse<>(true, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(ErrorType errorType) {
        return new ApiResponse<>(false, errorType.getErrorMessage(), null, LocalDateTime.now());
    }

    // Validation 예외 발생
    public static <T> ApiResponse<ErrorBody> validationError(String message, ErrorBody error) {
        return new ApiResponse<>(false, message, error, LocalDateTime.now());
    }
}