package com.kakao.mystery.domain.inventory.service;

import com.kakao.mystery.domain.inventory.dto.InventoryResponseDto;
import com.kakao.mystery.domain.inventory.entity.UserInventory;
import com.kakao.mystery.domain.inventory.repository.UserInventoryRepository;
import com.kakao.mystery.domain.item.entity.Item;
import com.kakao.mystery.domain.item.repository.ItemRepository;
import com.kakao.mystery.domain.session.entity.GameSession;
import com.kakao.mystery.domain.session.repository.GameSessionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    private final UserInventoryRepository inventoryRepository;
    private final GameSessionRepository sessionRepository;
    private final ItemRepository itemRepository;

    public void acquireItem(Long userId, Long sessionId, String itemId) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다."));

        // 1. 권한 체크 (403)
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 세션이 아닙니다.");
        }

        // 2. 세션 상태 체크 (409)
        if (session.getEndTime() != null) {
            throw new RuntimeException("이미 종료된 세션입니다.");
        }

        // 3. 아이템 획득 규칙 체크 (ITEM_02는 대화 보상 전용)
        if ("ITEM_02".equals(itemId)) {
            throw new RuntimeException("이 아이템은 직접 획득할 수 없습니다.");
        }

        // 4. 중복 체크 (409)
        if (inventoryRepository.existsByGameSessionIdAndItemId(sessionId, itemId)) {
            throw new RuntimeException("이미 획득한 아이템입니다.");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        inventoryRepository.save(new UserInventory(session, item));
    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getInventoryList(Long sessionId) {
        return inventoryRepository.findAllByGameSessionId(sessionId).stream()
                .map(inv -> new InventoryResponseDto(
                        inv.getItem().getId(),
                        inv.getItem().getName(),
                        inv.getObtainedAt()))
                .collect(Collectors.toList());
    }
}