package com.kakao.mystery.domain.npc.service;

import com.kakao.mystery.domain.inventory.entity.UserInventory;
import com.kakao.mystery.domain.inventory.repository.UserInventoryRepository;
import com.kakao.mystery.domain.item.repository.ItemRepository;
import com.kakao.mystery.domain.npc.dto.ChatApiResponse;
import com.kakao.mystery.domain.session.entity.ChatLog;
import com.kakao.mystery.domain.session.entity.GameSession;
import com.kakao.mystery.domain.session.entity.NpcStatus;
import com.kakao.mystery.domain.session.repository.ChatLogRepository;
import com.kakao.mystery.domain.session.repository.GameSessionRepository;
import com.kakao.mystery.domain.session.repository.NpcStatusRepository;
import com.kakao.mystery.global.config.dto.AiRequest;
import com.kakao.mystery.global.config.dto.AiResponse;
import com.kakao.mystery.global.config.dto.AiSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    @Qualifier("aiWebClient")
    private final WebClient aiWebClient;

    private final NpcStatusRepository npcStatusRepository;
    private final ChatLogRepository chatLogRepository;
    private final UserInventoryRepository inventoryRepository;
    private final GameSessionRepository gameSessionRepository;
    private final ItemRepository itemRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ChatApiResponse processChat(Long sessionId, String npcName, String userMessage, Long userId) {
        // 1. 기초 데이터 조회
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        // ✅ 2. 보안 검증: 현재 요청한 사용자가 세션의 소유자인지 확인
        if (!session.getUser().getId().equals(userId)) {
            log.warn("==> [Security Alert]: User {} tried to access Session {} owned by User {}",
                    userId, sessionId, session.getUser().getId());
            throw new SecurityException("해당 세션에 대한 접근 권한이 없습니다.");
        }

        NpcStatus status = npcStatusRepository.findByGameSessionIdAndNpcName(sessionId, npcName)
                .orElseThrow(() -> new IllegalArgumentException("NPC 상태를 찾을 수 없습니다."));

        // 3. AI 서버와 대화 통신
        AiResponse aiResponse = callAiApiInternal(sessionId, npcName, userMessage, status);

        // 4. 수치 업데이트
        session.decreaseQuestions();
        status.updateStats(aiResponse.statChanges().suspicion(), aiResponse.statChanges().affection());
        status.updateConfession(aiResponse.isConfessed());

        // 5. 대화 로그 저장
        saveChatLogs(session, npcName, userMessage, aiResponse.npcResponse());

        // 6. 요약 로직 실행 (5회 단위)
        if (status.getChatCount() > 0 && status.getChatCount() % 5 == 0) {
            triggerConversationSummary(sessionId, npcName, status);
        }

        // 7. 아이템 지급 로직
        List<ChatApiResponse.RewardDto> rewards = new ArrayList<>();
        String acquiredItemId = aiResponse.acquiredItem();

        if (acquiredItemId != null && !acquiredItemId.trim().isEmpty() && status.getAffectionScore() >= 30) {
            itemRepository.findById(acquiredItemId).ifPresent(item -> {
                UserInventory inventory = new UserInventory(session, item);
                inventoryRepository.save(inventory);

                rewards.add(new ChatApiResponse.RewardDto(
                        item.getId(),
                        item.getName(),
                        LocalDateTime.now().format(DATE_FORMATTER)
                ));
                log.info("==> [Item Awarded]: Item {} successfully granted.", item.getId());
            });
        }

        return new ChatApiResponse(
                session.getRemainingQuestions(),
                npcName,
                aiResponse.npcResponse(),
                rewards.stream().map(ChatApiResponse.RewardDto::itemId).collect(Collectors.toList()),
                new ChatApiResponse.NpcStateDto(status.getSuspicionScore(), status.getAffectionScore(), status.getIsConfessed()),
                rewards,
                session.getRemainingQuestions() <= 0 || status.getIsConfessed(),
                status.getIsConfessed() ? "CONFESSION_ENDING" : null
        );
    }

    private void triggerConversationSummary(Long sessionId, String npcName, NpcStatus status) {
        String koreanName = convertToKoreanName(npcName);

        List<AiRequest.ChatLogDto> fullHistory = chatLogRepository.findTop10ByGameSessionIdAndNpcNameOrderByCreatedAtDesc(sessionId, npcName)
                .stream()
                .map(log -> new AiRequest.ChatLogDto(log.getRole(), log.getMessage()))
                .collect(Collectors.toList());

        AiRequest.SummaryRequest summaryRequest = new AiRequest.SummaryRequest(
                koreanName,
                status.getConversationSummary() != null ? status.getConversationSummary() : "",
                fullHistory
        );

        AiSummaryResponse summaryResponse = aiWebClient.post()
                .uri("/api/summary")
                .bodyValue(summaryRequest)
                .retrieve()
                .bodyToMono(AiSummaryResponse.class)
                .block();

        if (summaryResponse != null) {
            status.updateSummary(summaryResponse.updatedSummary());
            log.info("==> [Summary Update Success] for {} ({})", koreanName, npcName);
        }
    }

    private void saveChatLogs(GameSession session, String npcName, String userMsg, String npcMsg) {
        chatLogRepository.save(ChatLog.builder().gameSession(session).npcName(npcName).role("USER").message(userMsg).build());
        chatLogRepository.save(ChatLog.builder().gameSession(session).npcName(npcName).role("NPC").message(npcMsg).build());
    }

    private AiResponse callAiApiInternal(Long sessionId, String npcName, String userMessage, NpcStatus status) {
        String koreanName = convertToKoreanName(npcName);

        List<AiRequest.ChatLogDto> recentLogs = chatLogRepository.findTop5ByGameSessionIdAndNpcNameOrderByCreatedAtDesc(sessionId, npcName)
                .stream()
                .map(log -> new AiRequest.ChatLogDto(log.getRole(), log.getMessage()))
                .collect(Collectors.toList());

        List<AiRequest.ItemDto> inventoryDtos = inventoryRepository.findAllByGameSessionId(sessionId)
                .stream()
                .map(inv -> new AiRequest.ItemDto(
                        inv.getItem().getId(),
                        inv.getItem().getName(),
                        inv.getObtainedAt().format(DATE_FORMATTER)
                ))
                .collect(Collectors.toList());

        AiRequest aiRequest = new AiRequest(
                koreanName,
                userMessage,
                sessionId,
                new AiRequest.NpcStatusDto(status.getSuspicionScore(), status.getAffectionScore(), status.getIsConfessed()),
                inventoryDtos,
                new AiRequest.ContextDto(status.getConversationSummary() != null ? status.getConversationSummary() : "", recentLogs)
        );

        log.info("==> [AI Request]: {}", aiRequest);

        AiResponse response = aiWebClient.post()
                .uri("/api/response")
                .bodyValue(aiRequest)
                .retrieve()
                .bodyToMono(AiResponse.class)
                .block();

        log.info(" <== [AI Response]: {}", response);

        return response;
    }

    private String convertToKoreanName(String npcId) {
        return switch (npcId.toUpperCase()) {
            case "APEACH" -> "어피치";
            case "RYAN" -> "라이언";
            case "MUZI" -> "무지";
            case "FRODO" -> "프로도";
            default -> npcId;
        };
    }
}