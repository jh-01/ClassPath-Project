package com.example.classpath.domain.notice.dto;

import com.example.classpath.domain.notice.entity.Notice;
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

    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.userId = notice.getUser().getId();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
        this.createdAt = notice.getCreatedAt();
        this.modifiedAt = notice.getModifiedAt();
    }

}
