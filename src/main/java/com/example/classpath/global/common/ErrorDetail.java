package com.example.classpath.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDetail {
    private String field;
    private String message;
}