package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ThinkingToolReportResponse {

    private LocalDate from;
    private LocalDate to;

    private long thinkingToolCount;
    private double averageBeforeScore;
    private double averageAfterScore;
    private double averageImprovement;

    private List<ThinkingToolTagStatResponse> tagStats;
    private List<ThinkingToolSessionStatResponse> sessionStats;
}