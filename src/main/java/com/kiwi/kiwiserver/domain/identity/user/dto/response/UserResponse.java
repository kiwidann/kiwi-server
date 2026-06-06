package com.kiwi.kiwiserver.domain.identity.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long userId;
    private Long accountId;
    private String nickname;
    private String profileImageUrl;
    private Integer kiwiBalance;
}
