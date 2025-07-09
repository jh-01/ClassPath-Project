package com.example.classpath.domain.user.repository;

import com.example.classpath.domain.user.dto.QUserResponse;
import com.example.classpath.domain.user.dto.UserResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.classpath.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class QUserRepositoryImpl implements QUserRepository{
    private final JPAQueryFactory queryFactory;

    // 학번으로 찾기
    @Override
    public UserResponse findUserByUserNumber(String userNumber){
        return queryFactory.select(new QUserResponse(
                user.id,
                user.userNumber,
                user.name,
                user.role,
                user.createdAt,
                user.modifiedAt
        ))
                .from(user)
                .where(user.userNumber.eq(userNumber))
                .fetchOne();
    }

    // 아이디로 찾기
    @Override
    public UserResponse findUserById(Long id){
        return queryFactory.select(new QUserResponse(
                user.id,
                user.userNumber,
                user.name,
                user.role,
                user.createdAt,
                user.modifiedAt
        ))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    // 전체 페이징 조회
    @Override
    public Page<UserResponse> findAllUsers(Pageable pageable){
        List<UserResponse> users = queryFactory.select(new QUserResponse(
                user.id,
                user.userNumber,
                user.name,
                user.role,
                user.createdAt,
                user.modifiedAt
        ))
                .from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(user.count())
                .from(user)
                .fetchOne();

        return new PageImpl<>(users, pageable, total);
    }
}
