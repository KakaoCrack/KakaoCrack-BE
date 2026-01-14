package com.kakao.mystery.domain.session.repository;

import com.kakao.mystery.domain.session.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    // 필요 시 유저 ID로 진행 중인 세션 찾기 등의 메서드 추가 가능
}