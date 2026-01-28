package com.kakao.mystery.domain.session.repository;

import com.kakao.mystery.domain.session.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    // 기존: AI 서버에 전달할 최근 대화 내역 (보통 5개)
    List<ChatLog> findTop5ByGameSessionIdAndNpcNameOrderByCreatedAtDesc(Long sessionId, String npcName);

    // ✅ 추가: 요약(Summary)을 위해 더 넓은 범위의 대화 내역(10개) 추출
    List<ChatLog> findTop10ByGameSessionIdAndNpcNameOrderByCreatedAtDesc(Long sessionId, String npcName);
}