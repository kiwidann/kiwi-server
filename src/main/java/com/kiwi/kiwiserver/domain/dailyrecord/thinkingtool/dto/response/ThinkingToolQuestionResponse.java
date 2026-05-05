package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.QuestionInputType;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;

public record ThinkingToolQuestionResponse(
        Long questionId,
        ThinkingToolCode toolCode,
        String code,
        String questionText,
        Integer displayOrder,
        QuestionInputType inputType,
        boolean isRequired
) {
}