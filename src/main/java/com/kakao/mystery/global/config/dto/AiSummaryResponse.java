package com.kakao.mystery.global.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiSummaryResponse(
        @JsonProperty("npcName") String npcName,
        @JsonProperty("updatedSummary") String updatedSummary
) {}