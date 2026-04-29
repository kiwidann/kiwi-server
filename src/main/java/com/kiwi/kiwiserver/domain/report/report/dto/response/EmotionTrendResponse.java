package com.kiwi.kiwiserver.domain.report.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class EmotionTrendResponse {

    private LocalDate from;
    private LocalDate to;

    private double averageEmotionScore;
    private Integer lowestEmotionScore;
    private Integer highestEmotionScore;

    private List<EmotionTrendPointResponse> points;
}