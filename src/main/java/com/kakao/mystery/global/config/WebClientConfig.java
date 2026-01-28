package com.kakao.mystery.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // 카카오 인증 등을 위한 일반 WebClient
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    // AI 서버 통신을 위한 전용 WebClient (포트 8080)
    @Bean(name = "aiWebClient")
    public WebClient aiWebClient() {
        return WebClient.builder()
                .baseUrl("http://13.62.102.106:8000") // 여기에 AI 서버 IP 주소를 넣으세요.
                .build();
    }
}