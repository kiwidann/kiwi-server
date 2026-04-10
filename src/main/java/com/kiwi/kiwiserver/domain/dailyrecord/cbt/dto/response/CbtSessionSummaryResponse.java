package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import java.time.Instant;
import java.time.OffsetDateTime;

public record CbtSessionSummaryResponse(
        Long cbtSessionId,
        Long tagId,
        String tagName,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt
) {
}