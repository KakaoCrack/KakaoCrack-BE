package com.kakao.mystery.domain.session.entity;

import com.kakao.mystery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
@Getter
@NoArgsConstructor
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 설계도의 Ref:"users"."id" < "game_sessions"."user_id" 반영
    private User user;

    @Column(name = "current_question_count")
    private Integer currentQuestionCount = 20;

    @Column(name = "start_time", insertable = false, updatable = false)
    private LocalDateTime startTime; // DB의 DEFAULT CURRENT_TIMESTAMP 사용

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "final_score")
    private Integer finalScore;

    @Column(name = "ending_type", length = 50)
    private String endingType;

    @Builder
    public GameSession(User user, Integer currentQuestionCount) {
        this.user = user;
        this.currentQuestionCount = (currentQuestionCount != null) ? currentQuestionCount : 20;
    }
}