package com.kiwi.kiwiserver.domain.item.dto.response;

public record ItemResponse(
        Long itemId,
        Long itemCategoryId,
        String itemCategoryName,
        String name,
        String description,
        String imageUrl,
        int price,
        boolean isActive,
        boolean owned,
        boolean equipped
) {
}