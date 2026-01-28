package com.kakao.mystery.domain.npc.dto;

import java.util.List;

public record ChatApiResponse(
        int remainingQuestions,
        String npcName,
        String reply,
        List<String> linkedEvidenceItemIds,
        NpcStateDto state,
        List<RewardDto> rewards,
        boolean gameOver,
        String ending
) {
    public record NpcStateDto(int suspicionScore, int affectionScore, boolean isConfessed) {}

    public record RewardDto(String itemId, String name, String obtainedAt) {}
}
