package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

public record CbtQuestionResponse(
        Long questionId,
        String code,
        String questionText,
        Integer displayOrder,
        boolean isRequired
) {
}