package com.kiwi.kiwiserver.domain.report.dto.response;

import lombok.Getter;

@Getter
public class KeywordStatResponse {

    private final String keyword;
    private final long count;
    private final double averageEmotionScore;

    public KeywordStatResponse(String keyword, long count, Double averageEmotionScore) {
        this.keyword = keyword;
        this.count = count;
        this.averageEmotionScore = averageEmotionScore == null ? 0.0 : averageEmotionScore;
    }
}