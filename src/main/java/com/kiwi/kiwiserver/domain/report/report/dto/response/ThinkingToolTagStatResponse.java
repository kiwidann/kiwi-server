package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Getter;

@Getter
public class ThinkingToolTagStatResponse {

    private final String tagName;
    private final long count;
    private final double averageImprovement;

    public ThinkingToolTagStatResponse(String tagName, long count, Double averageImprovement) {
        this.tagName = tagName;
        this.count = count;
        this.averageImprovement = averageImprovement == null ? 0.0 : averageImprovement;
    }
}