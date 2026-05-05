package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;

import java.time.Instant;
import java.util.List;

public record ThinkingToolSessionResponse(
        Long thinkingToolSessionId,
        Long recordId,
        Long tagId,
        String tagName,
        ThinkingToolCode toolCode,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt,
        List<ThinkingToolAnswerResponse> answers
) {
}