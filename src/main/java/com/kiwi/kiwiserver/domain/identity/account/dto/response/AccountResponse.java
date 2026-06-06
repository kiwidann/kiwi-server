package com.kiwi.kiwiserver.domain.identity.account.dto.response;

import java.time.Instant;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponse {

    private Long accountId;
    private String email;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
    private OffsetDateTime deletedAt;
}
