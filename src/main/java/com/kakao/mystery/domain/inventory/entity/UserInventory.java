package com.kakao.mystery.domain.inventory.entity;

import com.kakao.mystery.domain.item.entity.Item;
import com.kakao.mystery.domain.session.entity.GameSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_inventory")
public class UserInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private GameSession gameSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime obtainedAt = LocalDateTime.now();

    public UserInventory(GameSession session, Item item) {
        this.gameSession = session;
        this.item = item;
    }
}