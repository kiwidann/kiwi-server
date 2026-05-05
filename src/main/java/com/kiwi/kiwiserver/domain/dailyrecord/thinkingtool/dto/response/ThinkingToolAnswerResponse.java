package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.QuestionInputType;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;

public record ThinkingToolAnswerResponse(
        Long questionId,
        ThinkingToolCode toolCode,
        String code,
        String questionText,
        QuestionInputType inputType,
        String answerText,
        Integer answerValue
) {
}