package com.kakao.mystery.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", unique = true, nullable = false)
    private String kakaoId;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt; // DB의 DEFAULT CURRENT_TIMESTAMP 사용

    @Builder
    public User(String kakaoId, String nickname) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
    }
}