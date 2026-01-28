package com.kakao.mystery.domain.session.repository;

import com.kakao.mystery.domain.session.entity.NpcStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NpcStatusRepository extends JpaRepository<NpcStatus, Long> {

    // 특정 세션에 속한 모든 NPC 상태 조회 (기존 코드)
    List<NpcStatus> findAllByGameSessionId(Long sessionId);

    // ✅ 채팅 시 특정 NPC의 상태만 가져오기 위해 추가
    Optional<NpcStatus> findByGameSessionIdAndNpcName(Long sessionId, String npcName);
}