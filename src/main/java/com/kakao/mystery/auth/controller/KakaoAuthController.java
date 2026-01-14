package com.kakao.mystery.auth.controller;

import com.kakao.mystery.auth.dto.TokenRequestDto;
import com.kakao.mystery.auth.service.KakaoAuthService;
import com.kakao.mystery.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        KakaoAuthService.LoginResponse loginResponse = kakaoAuthService.login(code);


        Map<String, Object> response = new HashMap<>();
        response.put("user", loginResponse.getUser());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", loginResponse.getAccessToken());
        tokens.put("refreshToken", loginResponse.getRefreshToken());

        response.put("tokens", tokens);
        response.put("isNewUser", loginResponse.isNewUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRequestDto request) {
        try {
            Map<String, Object> tokens = kakaoAuthService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.success(tokens)); // 이전에 만든 공통 응답 활용
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        }
    }
}