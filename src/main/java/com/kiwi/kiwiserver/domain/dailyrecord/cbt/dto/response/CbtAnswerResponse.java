package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

public record CbtAnswerResponse(
        Long questionId,
        String code,
        String questionText,
        String answerText
) {
}