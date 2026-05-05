package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ThinkingToolSessionStatResponse {

    private final Long thinkingToolSessionId;
    private final LocalDate recordDate;
    private final Integer beforeEmotionScore;
    private final Integer afterEmotionScore;
    private final Integer improvement;
    private final String tagName;

    public ThinkingToolSessionStatResponse(
            Long thinkingToolSessionId,
            LocalDate recordDate,
            Integer beforeEmotionScore,
            Integer afterEmotionScore,
            Integer improvement,
            String tagName
    ) {
        this.thinkingToolSessionId = thinkingToolSessionId;
        this.recordDate = recordDate;
        this.beforeEmotionScore = beforeEmotionScore;
        this.afterEmotionScore = afterEmotionScore;
        this.improvement = improvement;
        this.tagName = tagName;
    }
}