package com.kakao.mystery.domain.user.repository;

import com.kakao.mystery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 카카오 고유 번호로 기존 가입 여부 확인
    Optional<User> findByKakaoId(String kakaoId);
}