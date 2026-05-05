package com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.QuestionInputType;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;

public record CbtAnswerResponse(
        Long questionId,
        ThinkingToolCode toolCode,
        String code,
        String questionText,
        QuestionInputType inputType,
        String answerText,
        Integer answerValue
) {
}