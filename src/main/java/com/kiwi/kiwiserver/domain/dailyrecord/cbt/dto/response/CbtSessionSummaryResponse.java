package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;

import java.time.Instant;

public record CbtSessionSummaryResponse(
        Long cbtSessionId,
        Long tagId,
        String tagName,
        ThinkingToolCode toolCode,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt
) {
}