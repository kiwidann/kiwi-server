package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public record CbtSessionResponse(
        Long cbtSessionId,
        Long recordId,
        Long tagId,
        String tagName,
        Integer beforeEmotionScore,
        Integer afterEmotionScore,
        Instant createdAt,
        List<CbtAnswerResponse> answers
) {
}