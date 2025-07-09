package com.example.classpath.domain.notice.repository;

import com.example.classpath.domain.notice.dto.NoticeResponseDto;
import com.example.classpath.domain.notice.dto.QNoticeResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.classpath.domain.notice.entity.QNotice.notice;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NoticeResponseDto> findNotices(Pageable pageable) {
        List<NoticeResponseDto> content = queryFactory.select(new QNoticeResponseDto(notice))
                .from(notice)
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(notice.count())
                .from(notice);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
