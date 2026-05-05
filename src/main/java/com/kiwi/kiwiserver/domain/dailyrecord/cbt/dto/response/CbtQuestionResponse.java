package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.QuestionInputType;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;

public record CbtQuestionResponse(
        Long questionId,
        ThinkingToolCode toolCode,
        String code,
        String questionText,
        Integer displayOrder,
        QuestionInputType inputType,
        boolean isRequired
) {
}