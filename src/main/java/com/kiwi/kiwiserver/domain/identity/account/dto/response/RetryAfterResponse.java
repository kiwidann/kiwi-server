package com.kiwi.kiwiserver.domain.identity.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetryAfterResponse {

    private long retryAfterSeconds;
}
