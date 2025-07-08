package com.example.classpath.domain.notice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {

    private final Long id;

    private final Long userId;

    private final String title;

    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public NoticeResponseDto(Long id, Long userId, String title, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
