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
    private String npcName;

    private Integer suspicionScore = 0;
    private Integer affectionScore = 0;
    private Boolean isConfessed = false;

    @Column(columnDefinition = "TEXT")
    private String conversationSummary = "";

    private Integer chatCount = 0;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    public NpcStatus(GameSession session, String npcName) {
        this.gameSession = session;
        this.npcName = npcName;
    }


    public void updateStats(int suspicionDelta, int affectionDelta) {

        this.suspicionScore = Math.max(0, Math.min(50, this.suspicionScore + suspicionDelta));
        this.affectionScore = Math.max(0, Math.min(50, this.affectionScore + affectionDelta));

        this.chatCount++; // 대화 횟수 증가
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateConfession(boolean isConfessed) {
        this.isConfessed = isConfessed;
    }

    public void updateSummary(String newSummary) {
        this.conversationSummary = newSummary;
    }
}