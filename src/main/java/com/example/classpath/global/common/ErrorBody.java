package com.example.classpath.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorBody {
    private ErrorDetail error;
}