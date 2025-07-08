package com.example.classpath.domain.notice.dto;

import lombok.Getter;

@Getter
public class NoticeUpdateRequestDto {

    private String title;

    private String contents;

    public NoticeUpdateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
