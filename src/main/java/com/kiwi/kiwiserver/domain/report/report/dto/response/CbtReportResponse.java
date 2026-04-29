package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CbtReportResponse {

    private LocalDate from;
    private LocalDate to;

    private long cbtCount;
    private double averageBeforeScore;
    private double averageAfterScore;
    private double averageImprovement;

    private List<CbtTagStatResponse> tagStats;
    private List<CbtSessionStatResponse> sessionStats;
}