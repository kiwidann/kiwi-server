package com.kiwi.kiwiserver.domain.item.dto.response;

import java.time.OffsetDateTime;

public record EquippedItemResponse(
        Long userEquippedItemId,
        Long itemCategoryId,
        String itemCategoryName,
        Long itemId,
        String itemName,
        String imageUrl,
        OffsetDateTime equippedAt
) {
}