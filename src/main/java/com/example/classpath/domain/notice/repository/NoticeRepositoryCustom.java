package com.example.classpath.domain.notice.repository;

import com.example.classpath.domain.notice.dto.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepositoryCustom {
    Page<NoticeResponseDto> findNotices(Pageable pageable);
}
