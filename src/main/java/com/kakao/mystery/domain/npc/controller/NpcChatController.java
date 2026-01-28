package com.kakao.mystery.domain.npc.controller;

import com.kakao.mystery.domain.npc.dto.ChatApiRequest;
import com.kakao.mystery.domain.npc.dto.ChatApiResponse;
import com.kakao.mystery.domain.npc.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class NpcChatController {

    private final ChatService chatService;


    @PostMapping("/{sessionId}/npcs/{npcName}/chat")
    public ResponseEntity<ChatApiResponse> chat(
            @AuthenticationPrincipal Long userId, // ✅ 필터에서 넣은 userId가 바로 주입됩니다.
            @PathVariable Long sessionId,
            @PathVariable String npcName,
            @RequestBody ChatApiRequest request) {

        // 서비스단에서 userId를 사용하여 세션 소유권 검증 및 비즈니스 로직 수행
        ChatApiResponse response = chatService.processChat(sessionId, npcName, request.message(), userId);

        return ResponseEntity.ok(response);
    }
}