package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CbtAnswerRequest(

        @NotNull(message = "질문 ID는 필수입니다")
        Long questionId,

        String answerText,

        @Min(value = 0, message = "답변 값은 0 이상이어야 합니다")
        @Max(value = 100, message = "답변 값은 100 이하여야 합니다")
        Integer answerValue
) {
}