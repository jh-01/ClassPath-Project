package com.example.classpath.domain.notice.repository;

import com.example.classpath.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

    @Modifying
    @Query("update Notice n set n.viewCount = n.viewCount + :increment where n.id = :noticeId")
    void increaseViewCount(@Param("noticeId") Long noticeId, @Param("increment") Long increment);

}
