package com.kiwi.kiwiserver.domain.item.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.item.entity.ItemCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_equipped_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_equipped_category",
                        columnNames = {"user_id", "item_category_id"}
                )
        }
)
public class UserEquippedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_equipped_item_id")
    private Long userEquippedItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id", nullable = false)
    private ItemCategory itemCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "equipped_at", nullable = false)
    private OffsetDateTime equippedAt;

    @Builder
    public UserEquippedItem(
            User user,
            ItemCategory itemCategory,
            Item item,
            OffsetDateTime equippedAt
    ) {
        this.user = user;
        this.itemCategory = itemCategory;
        this.item = item;
        this.equippedAt = equippedAt;
    }

    public static UserEquippedItem create(User user, ItemCategory itemCategory, Item item) {
        return UserEquippedItem.builder()
                .user(user)
                .itemCategory(itemCategory)
                .item(item)
                .equippedAt(OffsetDateTime.now())
                .build();
    }

    public void changeItem(Item item) {
        this.item = item;
        this.equippedAt = OffsetDateTime.now();
    }
}