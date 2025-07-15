package com.example.classpath.domain.notice.service;

import com.example.classpath.domain.notice.dto.NoticeResponseDto;
import com.example.classpath.domain.notice.entity.Notice;
import com.example.classpath.domain.notice.repository.NoticeRepository;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final ViewCountService viewCountService;

    // 공지 생성
    public NoticeResponseDto createNotice(Long userId, String title, String contents) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Notice notice = new Notice(user, title, contents);

        Notice saved = noticeRepository.save(notice);

        return new NoticeResponseDto(saved);
    }

    // 공지 전체 조회
    public Page<NoticeResponseDto> getNotices(Pageable pageable) {
        return noticeRepository.findNotices(pageable);
    }

    // 공지 단일 조회
    public NoticeResponseDto getNotice(Long userId, Long id) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        // 조회수 증가
        viewCountService.increaseViewCount(id, userId);

        // Redis에서 조회수 읽기
        Long viewCount = viewCountService.getViewCount(id);

        return new NoticeResponseDto(notice, viewCount);
    }

    // 조회수 top 10 공지 조회
    public List<NoticeResponseDto> getRankingNotices() {

        // Redis에서 조회수 랭킹 Top10 공지 ID 리스트를 가져옴
        List<Long> rankedIds = viewCountService.getRankingNoticeIds();

        List<NoticeResponseDto> result = new ArrayList<>();
        for (Long id : rankedIds) {
            Notice notice = noticeRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

            // Redis에서 조회수 가져오기
            Long viewCount = viewCountService.getViewCount(id);
            result.add(new NoticeResponseDto(notice, viewCount));
        }

        return result;
    }

    // 공지 수정
    @Transactional
    public NoticeResponseDto updateNotice(Long id, String title, String contents) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        // 공지사항 수정
        notice.updateNoticeInfo(title, contents);

        noticeRepository.save(notice);

        return new NoticeResponseDto(notice);
    }

    // 공지 삭제
    @Transactional
    public void deleteNotice(Long id) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        noticeRepository.delete(notice);
    }
}
