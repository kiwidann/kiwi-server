package com.kiwi.kiwiserver.domain.dailyrecord.record.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecordCreateRequest(

        @NotNull(message = "기록 날짜는 필수입니다")
        LocalDate recordDate,

        @NotNull(message = "감정 점수는 필수입니다")
        @Min(value = 1, message = "감정 점수는 1 이상이어야 합니다")
        @Max(value = 10, message = "감정 점수는 10 이하여야 합니다")
        Integer moodScore
) {
}