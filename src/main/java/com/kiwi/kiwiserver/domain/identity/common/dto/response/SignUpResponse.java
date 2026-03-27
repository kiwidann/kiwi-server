package com.kiwi.kiwiserver.domain.identity.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

    private Long accountId;
    private Long userId;
    private String email;
    private String nickname;
    private Integer kiwiBalance;
}
