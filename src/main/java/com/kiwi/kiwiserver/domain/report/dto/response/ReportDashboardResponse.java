package com.kiwi.kiwiserver.domain.report.dto.response;

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

    private long cbtCount;
    private double averageCbtBeforeScore;
    private double averageCbtAfterScore;
    private double averageCbtImprovement;

    private List<String> insights;
}