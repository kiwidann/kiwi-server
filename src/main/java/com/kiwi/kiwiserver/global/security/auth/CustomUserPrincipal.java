package com.kiwi.kiwiserver.global.security.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserPrincipal {

    private final Long accountId;
    private final Long userId;
    private final String email;
}