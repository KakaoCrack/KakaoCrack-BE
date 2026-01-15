package com.kakao.mystery.domain.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InventoryResponseDto {
    private String itemId;
    private String name;
    private LocalDateTime obtainedAt;
}