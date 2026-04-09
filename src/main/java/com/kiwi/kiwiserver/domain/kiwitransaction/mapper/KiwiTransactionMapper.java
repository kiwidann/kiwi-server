package com.kiwi.kiwiserver.domain.kiwitransaction.mapper;

import com.kiwi.kiwiserver.domain.kiwitransaction.dto.response.KiwiTransactionResponse;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTransaction;
import org.springframework.stereotype.Component;

@Component
public class KiwiTransactionMapper {

    public KiwiTransactionResponse toResponse(KiwiTransaction kiwiTransaction) {
        return new KiwiTransactionResponse(
                kiwiTransaction.getTxId(),
                kiwiTransaction.getAmount(),
                kiwiTransaction.getType(),
                kiwiTransaction.getCreatedAt()
        );
    }
}