package com.kiwi.kiwiserver.domain.item.entity;

import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id", nullable = false)
    private ItemCategory itemCategory;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Builder
    public Item(
            ItemCategory itemCategory,
            String name,
            String description,
            String imageUrl,
            int price,
            boolean isActive
    ) {
        this.itemCategory = itemCategory;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isActive = isActive;
    }

    public void updateItem(
            ItemCategory itemCategory,
            String name,
            String description,
            String imageUrl,
            int price,
            boolean isActive
    ) {
        this.itemCategory = itemCategory;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isActive = isActive;
    }
}