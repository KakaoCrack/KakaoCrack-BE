package com.kakao.mystery.domain.npc.dto;

/**
 * 유저의 채팅 요청을 담는 DTO
 * @param npcName 대화 대상 NPC (사실 URL 경로변수로 받으므로 생략 가능하지만, 명세서 규격에 따라 포함)
 * @param message 유저가 입력한 질문 내용
 */
public record ChatApiRequest(
        String npcName,
        String message
) {}