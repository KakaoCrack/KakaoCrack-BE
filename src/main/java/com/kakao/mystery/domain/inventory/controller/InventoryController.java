package com.kakao.mystery.domain.inventory.controller;

import com.kakao.mystery.domain.inventory.service.InventoryService;
import com.kakao.mystery.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/acquire")
    public ResponseEntity<?> acquire(@AuthenticationPrincipal Long userId,
                                     @PathVariable Long sessionId,
                                     @RequestBody Map<String, String> body) {
        inventoryService.acquireItem(userId, sessionId, body.get("itemId"));
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<?> getInventory(@PathVariable Long sessionId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getInventoryList(sessionId)));
    }
}