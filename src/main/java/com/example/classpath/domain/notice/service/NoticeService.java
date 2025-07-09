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

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    // 공지 생성
    public NoticeResponseDto createNotice(String userNumber, String title, String contents) {

        User user = userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        Notice notice = new Notice(user, title, contents);

        Notice saved = noticeRepository.save(notice);

        return new NoticeResponseDto(saved);
    }

    // 공지 단일 조회
    public NoticeResponseDto getNotice(Long id) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        return new NoticeResponseDto(notice);
    }

    @Transactional
    // 공지 수정
    public NoticeResponseDto updateNotice(Long id, String title, String contents) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        // 공지사항 수정
        notice.updateNoticeInfo(title, contents);

        noticeRepository.save(notice);

        return new NoticeResponseDto(notice);
    }

    @Transactional
    // 공지 삭제
    public void deleteNotice(Long id) {

        // 공지사항이 있는지 확인
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorType.NOTICE_NOT_FOUND));

        noticeRepository.delete(notice);
    }


}
