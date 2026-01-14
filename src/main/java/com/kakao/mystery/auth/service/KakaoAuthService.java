package com.kakao.mystery.auth.service;

import com.kakao.mystery.auth.jwt.JwtTokenProvider;
import com.kakao.mystery.domain.user.entity.User;
import com.kakao.mystery.domain.user.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final KakaoService kakaoService; // 필드명은 소문자
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(String code) {
        String kakaoToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(kakaoToken);

        String kakaoId = String.valueOf(userInfo.get("id"));
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
        String nickname = (properties != null) ? (String) properties.get("nickname") : "익명";

        boolean isNewUser = userRepository.findByKakaoId(kakaoId).isEmpty();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> userRepository.save(User.builder()
                        .kakaoId(kakaoId)
                        .nickname(nickname)
                        .build()));

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return LoginResponse.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(isNewUser)
                .build();
    }

    @Getter
    @Builder
    public static class LoginResponse {
        private User user;
        private String accessToken;
        private String refreshToken;
        private boolean isNewUser;
    }

    public Map<String, Object> refreshAccessToken(String refreshToken) {
        // 1. Refresh Token 검증
        if (jwtTokenProvider.validateToken(refreshToken)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

            // 2. 새로운 Access Token 생성
            String newAccessToken = jwtTokenProvider.createAccessToken(userId);

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", refreshToken); // 기존 Refresh Token 재사용 또는 갱신
            return tokens;
        }
        throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
    }
}