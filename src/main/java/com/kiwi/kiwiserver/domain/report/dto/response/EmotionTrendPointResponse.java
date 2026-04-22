package com.kiwi.kiwiserver.domain.report.dto.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EmotionTrendPointResponse {

    private final LocalDate recordDate;
    private final Integer emotionScore;

    public EmotionTrendPointResponse(LocalDate recordDate, Integer emotionScore) {
        this.recordDate = recordDate;
        this.emotionScore = emotionScore;
    }
}