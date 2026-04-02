package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request;

import jakarta.validation.constraints.NotNull;

public record CbtAnswerRequest(

        @NotNull(message = "질문 ID는 필수입니다")
        Long questionId,

        String answerText
) {
}