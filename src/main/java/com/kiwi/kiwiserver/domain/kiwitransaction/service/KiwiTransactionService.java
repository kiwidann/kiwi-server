package com.kiwi.kiwiserver.domain.kiwitransaction.service;

import com.kiwi.kiwiserver.domain.kiwitransaction.dto.response.KiwiTransactionResponse;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTransaction;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTxType;
import com.kiwi.kiwiserver.domain.kiwitransaction.mapper.KiwiTransactionMapper;
import com.kiwi.kiwiserver.domain.kiwitransaction.repository.KiwiTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KiwiTransactionService {

    private final KiwiTransactionRepository kiwiTransactionRepository;
    private final KiwiTransactionMapper kiwiTransactionMapper;

    public Page<KiwiTransactionResponse> getKiwiTransactions(Long userId, KiwiTxType type, Pageable pageable) {
        Page<KiwiTransaction> kiwiTransactions;

        if (type == null) {
            kiwiTransactions = kiwiTransactionRepository.findAllByUser_UserId(userId, pageable);
        } else {
            kiwiTransactions = kiwiTransactionRepository.findAllByUser_UserIdAndType(userId, type, pageable);
        }

        return kiwiTransactions.map(kiwiTransactionMapper::toResponse);
    }
}