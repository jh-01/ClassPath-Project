package com.example.classpath.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoticeCreateRequestDto {

    @NotBlank
    private final String title;

    @NotBlank
    private final String contents;

    public NoticeCreateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
