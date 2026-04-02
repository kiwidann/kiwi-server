package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CbtCreateRequest(

        @NotNull(message = "태그 ID는 필수입니다")
        Long tagId,

        @NotNull(message = "CBT 전 감정 점수는 필수입니다")
        @Min(value = 0, message = "CBT 전 감정 점수는 0 이상이어야 합니다")
        @Max(value = 10, message = "CBT 전 감정 점수는 10 이하여야 합니다")
        Integer beforeEmotionScore,

        @NotNull(message = "CBT 후 감정 점수는 필수입니다")
        @Min(value = 0, message = "CBT 후 감정 점수는 0 이상이어야 합니다")
        @Max(value = 10, message = "CBT 후 감정 점수는 10 이하여야 합니다")
        Integer afterEmotionScore,

        @NotNull(message = "답변 목록은 필수입니다")
        @Valid
        List<CbtAnswerRequest> answers
) {
}