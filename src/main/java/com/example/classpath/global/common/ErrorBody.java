package com.example.classpath.global.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorBody {
    private List<ErrorDetail> errors;

    public ErrorBody(List<ErrorDetail> errors) {
        this.errors = errors;
    }
}