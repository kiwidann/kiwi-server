package com.kiwi.kiwiserver.domain.identity.account.repository;

import java.util.Optional;

import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByAccountIdAndIsDeletedFalse(Long accountId);

    Optional<Account> findByEmailAndIsDeletedFalse(String email);
}