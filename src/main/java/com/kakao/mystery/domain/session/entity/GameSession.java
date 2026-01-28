package com.kakao.mystery.domain.session.entity;

import com.kakao.mystery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer remainingQuestions = 150; // 명세서 기준 20회
    private Integer gameProgress = 0;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private Integer finalScore;
    private String endingType;

    public GameSession(User user) {
        this.user = user;
    }

    // ✅ 질문 횟수 감소 메서드 추가
    public void decreaseQuestions() {
        if (this.remainingQuestions > 0) {
            this.remainingQuestions--;
        }
    }
}