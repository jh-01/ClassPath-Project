package com.example.classpath.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoticeCreateRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String contents;

    public NoticeCreateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
