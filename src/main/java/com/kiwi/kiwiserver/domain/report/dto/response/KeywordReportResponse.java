package com.kiwi.kiwiserver.domain.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class KeywordReportResponse {

    private LocalDate from;
    private LocalDate to;
    private List<KeywordStatResponse> keywords;
}