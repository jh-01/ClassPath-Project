package com.example.classpath.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ErrorBody {
    private List<ErrorDetail> error;
}