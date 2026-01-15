package com.kakao.mystery.domain.session.repository;

import com.kakao.mystery.domain.session.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}