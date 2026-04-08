package com.kiwi.kiwiserver.domain.item.dto.response;

import java.time.OffsetDateTime;

public record OwnedItemResponse(
        Long userItemId,
        Long itemId,
        Long itemCategoryId,
        String itemCategoryName,
        String name,
        String description,
        String imageUrl,
        int price,
        OffsetDateTime acquiredAt
) {
}