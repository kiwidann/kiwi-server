package com.kiwi.kiwiserver.domain.identity.account.mapper;

import com.kiwi.kiwiserver.domain.identity.account.dto.response.AccountResponse;
import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.identity.common.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(SignUpRequest request, String passwordHash) {
        return Account.create(
                request.getEmail(),
                passwordHash
        );
    }

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .email(account.getEmail())
                .isDeleted(account.getIsDeleted())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .deletedAt(account.getDeletedAt())
                .build();
    }
}
