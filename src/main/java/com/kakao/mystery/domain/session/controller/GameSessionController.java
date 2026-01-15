package com.kakao.mystery.domain.session.controller;

import com.kakao.mystery.domain.session.dto.SessionResponseDto;
import com.kakao.mystery.domain.session.service.GameSessionService;
import com.kakao.mystery.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class GameSessionController {
    private final GameSessionService gameSessionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SessionResponseDto>> startNewGame(@AuthenticationPrincipal Long userId) {
        SessionResponseDto response = gameSessionService.createSession(userId);
        return ResponseEntity.status(HttpStatus.CREATED) // 명세서 201 응답
                .body(ApiResponse.success(response));
    }
}