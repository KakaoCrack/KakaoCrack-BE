package com.kakao.mystery.domain.session.service;

import com.kakao.mystery.domain.session.dto.SessionResponseDto;
import com.kakao.mystery.domain.session.entity.GameSession;
import com.kakao.mystery.domain.session.entity.NpcStatus;
import com.kakao.mystery.domain.session.repository.GameSessionRepository;
import com.kakao.mystery.domain.session.repository.NpcStatusRepository;
import com.kakao.mystery.domain.user.entity.User;
import com.kakao.mystery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GameSessionService {
    private final GameSessionRepository sessionRepository;
    private final NpcStatusRepository npcStatusRepository;
    private final UserRepository userRepository;

    public SessionResponseDto createSession(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 1. 세션 생성
        GameSession session = sessionRepository.save(new GameSession(user));

        // 2. NPC 4명 초기화 (명세서 상수 기준)
        List<String> npcs = List.of("RYAN", "MUZI", "APEACH", "FRODO");
        for (String name : npcs) {
            npcStatusRepository.save(new NpcStatus(session, name));
        }

        return SessionResponseDto.from(session);
    }
}