package com.kiwi.kiwiserver.domain.identity.account.exception;

import lombok.Getter;

@Getter
public class TooManyVerificationRequestsException extends RuntimeException {

    private final long retryAfterSeconds;

    public TooManyVerificationRequestsException(long retryAfterSeconds) {
        super("인증 코드 재전송 요청이 너무 많습니다. 잠시 후 다시 시도해주세요");
        this.retryAfterSeconds = retryAfterSeconds;
    }
}