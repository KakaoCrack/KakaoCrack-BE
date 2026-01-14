package com.kakao.mystery.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final WebClient webClient;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    @Value("${kakao.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        Map<String, Object> response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code +
                        "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return (String) response.get("access_token");
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}