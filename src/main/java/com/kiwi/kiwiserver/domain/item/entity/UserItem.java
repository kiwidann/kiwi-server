package com.kiwi.kiwiserver.domain.item.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
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
        name = "user_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_items_user_item",
                        columnNames = {"user_id", "item_id"}
                )
        }
)
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_item_id")
    private Long userItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "acquired_at", nullable = false, updatable = false)
    private OffsetDateTime acquiredAt;

    @Column(name = "is_owned", nullable = false)
    private boolean isOwned;

    @Builder
    public UserItem(
            User user,
            Item item,
            OffsetDateTime acquiredAt,
            boolean isOwned
    ) {
        this.user = user;
        this.item = item;
        this.acquiredAt = acquiredAt;
        this.isOwned = isOwned;
    }

    public static UserItem create(User user, Item item) {
        return UserItem.builder()
                .user(user)
                .item(item)
                .acquiredAt(OffsetDateTime.now())
                .isOwned(true)
                .build();
    }
}