package com.kiwi.kiwiserver.domain.dailyrecord.cbt.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class CbtMapper {

    public TagResponse toTagResponse(Tag tag) {
        return new TagResponse(
                tag.getTagId(),
                tag.getName()
        );
    }

    public CbtQuestionResponse toQuestionResponse(CbtQuestion question) {
        return new CbtQuestionResponse(
                question.getQuestionId(),
                question.getToolCode(),
                question.getCode(),
                question.getQuestionText(),
                question.getDisplayOrder(),
                question.getInputType(),
                question.isRequired()
        );
    }

    public CbtAnswerResponse toAnswerResponse(CbtAnswer answer) {
        CbtQuestion question = answer.getQuestion();

        return new CbtAnswerResponse(
                question.getQuestionId(),
                question.getToolCode(),
                question.getCode(),
                question.getQuestionText(),
                question.getInputType(),
                answer.getAnswerText(),
                answer.getAnswerValue()
        );
    }

    public CbtSessionSummaryResponse toSessionSummaryResponse(CbtSession session) {
        return new CbtSessionSummaryResponse(
                session.getCbtSessionId(),
                session.getTag().getTagId(),
                session.getTag().getName(),
                session.getToolCode(),
                session.getBeforeEmotionScore(),
                session.getAfterEmotionScore(),
                session.getCreatedAt()
        );
    }

    public CbtSessionResponse toSessionResponse(CbtSession session, List<CbtAnswer> answers) {
        return new CbtSessionResponse(
                session.getCbtSessionId(),
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