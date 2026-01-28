package com.kakao.mystery.global.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AiRequest(
        @JsonProperty("npcName") String npcName,
        @JsonProperty("userMessage") String userMessage,
        @JsonProperty("sessionId") Long sessionId,
        @JsonProperty("status") NpcStatusDto npcStatus,
        @JsonProperty("userInventory") List<ItemDto> inventory,
        @JsonProperty("context") ContextDto context
) {
    public record NpcStatusDto(
            @JsonProperty("suspicionScore") Integer suspicionScore,
            @JsonProperty("affectionScore") Integer affectionScore,
            @JsonProperty("isConfessed") Boolean isConfessed
    ) {}

    public record ItemDto(
            @JsonProperty("itemId") String itemId,
            @JsonProperty("name") String name,
            @JsonProperty("obtainedAt") String obtainedAt
    ) {}

    public record ContextDto(
            @JsonProperty("summary") String summary,
            @JsonProperty("recentLogs") List<ChatLogDto> recentLogs
    ) {}

    public record ChatLogDto(
            @JsonProperty("role") String role,
            @JsonProperty("message") String message
    ) {}

    // 2번 요약 모델 전용 Request DTO
    public record SummaryRequest(
            @JsonProperty("npcName") String npcName,
            @JsonProperty("summary") String summary,
            @JsonProperty("recentLogs") List<ChatLogDto> recentLogs
    ) {}
}