package com.kakao.mystery.global.config.dto;

import java.util.List;

public record AiResponse(
        String npcResponse,
        StatChanges statChanges,
        String acquiredItem,
        Boolean isConfessed
) {
    public record StatChanges(Integer suspicion, Integer affection) {
        // ✅ 명시적 생성자를 추가하여 null이 들어오면 0으로 초기화합니다.
        public StatChanges {
            if (suspicion == null) suspicion = 0;
            if (affection == null) affection = 0;
        }
    }


}