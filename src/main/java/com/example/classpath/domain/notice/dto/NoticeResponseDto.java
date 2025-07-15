package com.example.classpath.domain.notice.dto;

import com.example.classpath.domain.notice.entity.Notice;
import com.querydsl.core.annotations.QueryProjection;
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

    private Long viewCount = 0L;

    @QueryProjection
    public NoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.userId = notice.getUser().getId();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
        this.createdAt = notice.getCreatedAt();
        this.modifiedAt = notice.getModifiedAt();
        this.viewCount = notice.getViewCount();
    }

    public NoticeResponseDto(Notice notice, Long viewCount) {
        this.id = notice.getId();
        this.userId = notice.getUser().getId();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
        this.createdAt = notice.getCreatedAt();
        this.modifiedAt = notice.getModifiedAt();
        this.viewCount = viewCount;
    }
}
