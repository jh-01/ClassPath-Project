package com.example.classpath.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorDetail {
    private String field;
    private String message;
}