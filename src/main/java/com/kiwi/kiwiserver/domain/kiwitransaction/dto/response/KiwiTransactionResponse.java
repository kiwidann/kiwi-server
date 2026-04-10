package com.kiwi.kiwiserver.domain.kiwitransaction.dto.response;

import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTxType;

import java.time.Instant;
import java.time.OffsetDateTime;

public record KiwiTransactionResponse(
        Long txId,
        int amount,
        KiwiTxType type,
        Instant createdAt
) {
}