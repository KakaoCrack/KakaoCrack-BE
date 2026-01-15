package com.kakao.mystery.domain.item.service;

import com.kakao.mystery.domain.item.dto.ItemResponseDto;
import com.kakao.mystery.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    public List<ItemResponseDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}