package com.kiwi.kiwiserver.domain.item.dto.response;

public record PurchaseItemResponse(
        Long itemId,
        String itemName,
        int itemPrice,
        int remainingKiwiBalance
) {
}