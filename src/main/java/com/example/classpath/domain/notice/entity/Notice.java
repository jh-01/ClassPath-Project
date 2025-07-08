package com.example.classpath.domain.notice.entity;

import com.example.classpath.domain.user.entity.User;
import com.example.classpath.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "notice")
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String contents;

    public Notice(User user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
    }

    public Notice() {

    }

    // 공지사항 수정 메서드
    public void updateNoticeInfo(String title, String contents) {
        if (title != null) {
            this.title = title;
        }
        if (contents != null) {
            this.contents = contents;
        }
    }
}
