package com.kakao.mystery.domain.session.repository;

import com.kakao.mystery.domain.session.entity.NpcStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NpcStatusRepository extends JpaRepository<NpcStatus, Long> {
    // 특정 세션에 속한 모든 NPC 상태를 가져올 때 사용
    List<NpcStatus> findAllByGameSessionId(Long sessionId);
}