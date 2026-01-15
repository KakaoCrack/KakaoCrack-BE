package com.kakao.mystery.domain.session.dto;

import com.kakao.mystery.domain.session.entity.GameSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SessionResponseDto {
    private Long sessionId;
    private Integer remainingQuestions;
    private LocalDateTime startTime;
    private Integer gameProgress;

    public static SessionResponseDto from(GameSession session) {
        return new SessionResponseDto(
                session.getId(),
                session.getRemainingQuestions(),
                session.getStartTime(),
                session.getGameProgress()
        );
    }
}