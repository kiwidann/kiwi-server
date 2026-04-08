package com.kiwi.kiwiserver.domain.item.dto.request;

import jakarta.validation.constraints.NotNull;

public record EquipItemRequest(
        @NotNull
        Long itemId
) {
}