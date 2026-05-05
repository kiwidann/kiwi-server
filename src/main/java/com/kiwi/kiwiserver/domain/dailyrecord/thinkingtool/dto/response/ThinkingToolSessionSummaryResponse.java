package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;

import java.time.Instant;

public record ThinkingToolSessionSummaryResponse(
        Long thinkingToolSessionId,
        Long tagId,
        String tagName,
        ThinkingToolCode toolCode,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt
) {
}