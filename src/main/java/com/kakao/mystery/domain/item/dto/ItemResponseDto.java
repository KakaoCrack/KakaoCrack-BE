package com.kakao.mystery.domain.item.dto;

import com.kakao.mystery.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponseDto {
    private String itemId;
    private String name;
    private String description;

    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(item.getId(), item.getName(), item.getDescription());
    }
}