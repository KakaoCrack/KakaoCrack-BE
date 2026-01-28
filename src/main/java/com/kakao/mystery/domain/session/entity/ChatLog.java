package com.kakao.mystery.domain.session.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_logs")
public class ChatLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession gameSession;

    @Column(name = "npc_name", nullable = false)
    private String npcName;

    @Column(name = "role", nullable = false) // "USER" 또는 "NPC"
    private String role;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    @Builder
    public ChatLog(GameSession gameSession, String npcName, String role, String message) {
        this.gameSession = gameSession;
        this.npcName = npcName;
        this.role = role;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}