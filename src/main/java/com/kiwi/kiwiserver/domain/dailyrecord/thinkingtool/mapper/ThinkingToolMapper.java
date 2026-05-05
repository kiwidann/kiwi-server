package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ThinkingToolMapper {

    public TagResponse toTagResponse(Tag tag) {
        return new TagResponse(
                tag.getTagId(),
                tag.getName()
        );
    }

    public ThinkingToolQuestionResponse toQuestionResponse(ThinkingToolQuestion question) {
        return new ThinkingToolQuestionResponse(
                question.getQuestionId(),
                question.getToolCode(),
                question.getCode(),
                question.getQuestionText(),
                question.getDisplayOrder(),
                question.getInputType(),
                question.isRequired()
        );
    }

    public ThinkingToolAnswerResponse toAnswerResponse(ThinkingToolAnswer answer) {
        ThinkingToolQuestion question = answer.getQuestion();

        return new ThinkingToolAnswerResponse(
                question.getQuestionId(),
                question.getToolCode(),
                question.getCode(),
                question.getQuestionText(),
                question.getInputType(),
                answer.getAnswerText(),
                answer.getAnswerValue()
        );
    }

    public ThinkingToolSessionSummaryResponse toSessionSummaryResponse(ThinkingToolSession session) {
        return new ThinkingToolSessionSummaryResponse(
                session.getThinkingToolSessionId(),
                session.getTag().getTagId(),
                session.getTag().getName(),
                session.getToolCode(),
                session.getBeforeEmotionScore(),
                session.getAfterEmotionScore(),
                session.getCreatedAt()
        );
    }

    public ThinkingToolSessionResponse toSessionResponse(ThinkingToolSession session, List<ThinkingToolAnswer> answers) {
        return new ThinkingToolSessionResponse(
                session.getThinkingToolSessionId(),
                session.getRecord().getRecordId(),
                session.getTag().getTagId(),
                session.getTag().getName(),
                session.getToolCode(),
                session.getBeforeEmotionScore(),
                session.getAfterEmotionScore(),
                session.getCreatedAt(),
                answers.stream()
                        .sorted(Comparator.comparing(answer -> answer.getQuestion().getDisplayOrder()))
                        .map(this::toAnswerResponse)
                        .toList()
        );
    }
}