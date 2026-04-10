package com.kiwi.kiwiserver.domain.item.entity;

import com.kiwi.kiwiserver.global.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_categories")
public class ItemCategory extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_category_id")
    private Long itemCategoryId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Builder
    public ItemCategory(String name, OffsetDateTime createdAt) {
        this.name = name;
    }
}