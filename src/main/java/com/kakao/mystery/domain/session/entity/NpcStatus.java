package com.kakao.mystery.domain.session.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "npc_status", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id", "npc_name"})
})
public class NpcStatus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession gameSession;

    @Column(name = "npc_name")
    private String npcName; // RYAN, MUZI, APEACH, FRODO

    private Integer suspicionScore = 0;
    private Integer affectionScore = 0;
    private Boolean isConfessed = false;

    @Column(columnDefinition = "TEXT")
    private String conversationSummary = ""; // 캐릭터별 요약

    private LocalDateTime lastUpdated = LocalDateTime.now();

    public NpcStatus(GameSession session, String npcName) {
        this.gameSession = session;
        this.npcName = npcName;
    }
}