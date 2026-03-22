package com.kiwi.kiwiserver.domain.identity.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private Long accountId;
    private Long userId;
    private String email;
    private String nickname;
    private String accessToken;
}
