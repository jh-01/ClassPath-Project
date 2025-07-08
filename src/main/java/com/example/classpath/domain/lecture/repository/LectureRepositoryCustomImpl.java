package com.example.classpath.domain.lecture.repository;

import com.example.classpath.domain.enrollment.entity.QEnrollment;
import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.dto.LectureSearchCondition;
import com.example.classpath.domain.lecture.dto.QLectureResponse;
import com.example.classpath.domain.lecture.entity.QLecture;
import com.example.classpath.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.classpath.domain.enrollment.entity.QEnrollment.enrollment;
import static com.example.classpath.domain.lecture.entity.QLecture.*;
public class LectureRepositoryCustomImpl implements LectureRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public LectureRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<LectureResponse> searchLecture(LectureSearchCondition condition, Pageable pageable) {

        List<LectureResponse> content = queryFactory.select(new QLectureResponse(lecture))
                .from(lecture)
                .where(
                        nameContain(condition.getName()),
                        codeContain(condition.getCode())
                )
                .orderBy(lecture.code.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(lecture.count())
                .from(lecture)
                .where(
                        nameContain(condition.getName()),
                        codeContain(condition.getCode())
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<LectureResponse> findAllUserLecture(User user) {
        return queryFactory.select(new QLectureResponse(lecture))
                .from(enrollment)
                .join(enrollment.lecture,lecture)
                .where(enrollment.user.eq(user))
                .fetch();
    }

    private BooleanExpression nameContain(String name) {
        return name != null ? lecture.name.contains(name) : null;
    }

    private BooleanExpression codeContain(String code) {
        return code != null ? lecture.code.contains(code) : null;
    }
}
