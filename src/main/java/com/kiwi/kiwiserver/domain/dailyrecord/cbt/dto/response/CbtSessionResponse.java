package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;

import java.time.Instant;
import java.util.List;

public record CbtSessionResponse(
        Long cbtSessionId,
        Long recordId,
        Long tagId,
        String tagName,
        ThinkingToolCode toolCode,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt,
        List<CbtAnswerResponse> answers
) {
}