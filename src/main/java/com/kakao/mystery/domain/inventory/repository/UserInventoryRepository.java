package com.kakao.mystery.domain.inventory.repository;

import com.kakao.mystery.domain.inventory.entity.UserInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
    List<UserInventory> findAllByGameSessionId(Long sessionId);
    boolean existsByGameSessionIdAndItemId(Long sessionId, String itemId);
}