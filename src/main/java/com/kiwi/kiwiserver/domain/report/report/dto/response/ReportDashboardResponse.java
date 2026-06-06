package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReportDashboardResponse {

    private LocalDate from;
    private LocalDate to;

    private long recordCount;
    private double averageEmotionScore;
    private Integer lowestEmotionScore;
    private Integer highestEmotionScore;

    private List<KeywordStatResponse> topKeywords;

    private long thinkingToolCount;
    private double averageThinkingToolBeforeScore;
    private double averageThinkingToolAfterScore;
    private double averageThinkingToolImprovement;

    private List<ThinkingToolStatResponse> topThinkingTools;

    private List<String> insights;
}