package com.kiwi.kiwiserver.domain.kiwitransaction.repository;

import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTransaction;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTxType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KiwiTransactionRepository extends JpaRepository<KiwiTransaction, Long> {

    Page<KiwiTransaction> findAllByUser_UserId(Long userId, Pageable pageable);

    Page<KiwiTransaction> findAllByUser_UserIdAndType(Long userId, KiwiTxType type, Pageable pageable);
}