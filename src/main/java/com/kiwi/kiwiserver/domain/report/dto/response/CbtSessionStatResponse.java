package com.kiwi.kiwiserver.domain.report.dto.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CbtSessionStatResponse {

    private final Long cbtSessionId;
    private final LocalDate recordDate;
    private final Integer beforeEmotionScore;
    private final Integer afterEmotionScore;
    private final Integer improvement;
    private final String tagName;

    public CbtSessionStatResponse(
            Long cbtSessionId,
            LocalDate recordDate,
            Integer beforeEmotionScore,
            Integer afterEmotionScore,
            Integer improvement,
            String tagName
    ) {
        this.cbtSessionId = cbtSessionId;
        this.recordDate = recordDate;
        this.beforeEmotionScore = beforeEmotionScore;
        this.afterEmotionScore = afterEmotionScore;
        this.improvement = improvement;
        this.tagName = tagName;
    }
}