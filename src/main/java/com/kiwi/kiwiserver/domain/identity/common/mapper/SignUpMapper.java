package com.kiwi.kiwiserver.domain.identity.common.mapper;

import com.kiwi.kiwiserver.domain.identity.common.dto.response.SignUpResponse;
import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SignUpMapper {

    public SignUpResponse toResponse(Account account, User user) {
        return SignUpResponse.builder()
                .accountId(account.getAccountId())
                .userId(user.getUserId())
                .email(account.getEmail())
                .nickname(user.getNickname())
                .kiwiBalance(user.getKiwiBalance())
                .build();
    }
}
