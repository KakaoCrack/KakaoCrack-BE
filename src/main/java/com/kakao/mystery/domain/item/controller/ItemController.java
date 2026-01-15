package com.kakao.mystery.domain.item.controller;

import com.kakao.mystery.domain.item.dto.ItemResponseDto;
import com.kakao.mystery.domain.item.service.ItemService;
import com.kakao.mystery.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponseDto>>> getItems() {
        List<ItemResponseDto> response = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}