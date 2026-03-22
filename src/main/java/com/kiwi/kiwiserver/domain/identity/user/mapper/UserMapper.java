package com.kiwi.kiwiserver.domain.identity.user.mapper;

import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.identity.common.dto.request.SignUpRequest;
import com.kiwi.kiwiserver.domain.identity.user.dto.request.CreateUserRequest;
import com.kiwi.kiwiserver.domain.identity.user.dto.response.UserResponse;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(SignUpRequest request, Account account) {
        return User.create(
                account,
                request.getNickname(),
                null
        );
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .accountId(user.getAccount().getAccountId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .kiwiBalance(user.getKiwiBalance())
                .build();
    }
}