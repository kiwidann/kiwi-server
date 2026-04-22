package com.kiwi.kiwiserver.domain.report.dto.response;

import lombok.Getter;

@Getter
public class CbtTagStatResponse {

    private final String tagName;
    private final long count;
    private final double averageImprovement;

    public CbtTagStatResponse(String tagName, long count, Double averageImprovement) {
        this.tagName = tagName;
        this.count = count;
        this.averageImprovement = averageImprovement == null ? 0.0 : averageImprovement;
    }
}