package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.service;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.request.ThinkingToolAnswerRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.request.ThinkingToolCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.*;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.exception.ThinkingToolErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.mapper.ThinkingToolMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.repository.*;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import com.kiwi.kiwiserver.domain.dailyrecord.record.exception.RecordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.record.repository.RecordRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThinkingToolService {

    private final RecordRepository recordRepository;
    private final TagRepository tagRepository;
    private final ThinkingToolQuestionRepository questionRepository;
    private final ThinkingToolSessionRepository sessionRepository;
    private final ThinkingToolAnswerRepository answerRepository;
    private final ThinkingToolMapper thinkingToolMapper;

    public List<TagResponse> getTags() {
        return tagRepository.findAllByOrderByNameAsc()
                .stream()
                .map(thinkingToolMapper::toTagResponse)
                .toList();
    }

    public List<ThinkingToolQuestionResponse> getQuestions(ThinkingToolCode toolCode) {
        return questionRepository.findAllByToolCodeAndIsActiveTrueOrderByDisplayOrderAsc(toolCode)
                .stream()
                .map(thinkingToolMapper::toQuestionResponse)
                .toList();
    }

    @Transactional
    public ThinkingToolSessionResponse createThinkingTool(Long userId, Long recordId, ThinkingToolCreateRequest request) {
        Record record = getOwnedRecord(userId, recordId);

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_TAG_NOT_FOUND));

        validateAnswerRequests(request.answers());

        List<Long> questionIds = request.answers().stream()
                .map(ThinkingToolAnswerRequest::questionId)
                .toList();

        List<ThinkingToolQuestion> questions = questionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_QUESTION_NOT_FOUND);
        }

        validateQuestionsBelongToTool(questions, request.toolCode());

        Map<Long, ThinkingToolQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(ThinkingToolQuestion::getQuestionId, Function.identity()));

        List<ThinkingToolQuestion> requiredQuestions =
                questionRepository.findAllByToolCodeAndIsActiveTrueOrderByDisplayOrderAsc(request.toolCode())
                        .stream()
                        .filter(ThinkingToolQuestion::isRequired)
                        .toList();

        validateRequiredQuestions(requiredQuestions, request.answers());
        validateAnswerByInputType(questionMap, request.answers());

        ThinkingToolSession session = ThinkingToolSession.builder()
                .record(record)
                .tag(tag)
                .toolCode(request.toolCode())
                .beforeEmotionScore(request.beforeEmotionScore())
                .afterEmotionScore(request.afterEmotionScore())
                .build();

        ThinkingToolSession savedSession = sessionRepository.save(session);

        List<ThinkingToolAnswer> answers = request.answers().stream()
                .map(answerRequest -> new ThinkingToolAnswer(
                        savedSession,
                        questionMap.get(answerRequest.questionId()),
                        answerRequest.answerText(),
                        answerRequest.answerValue()
                ))
                .toList();

        List<ThinkingToolAnswer> savedAnswers = answerRepository.saveAll(answers);

        return thinkingToolMapper.toSessionResponse(savedSession, savedAnswers);
    }

    public List<ThinkingToolSessionSummaryResponse> getThinkingToolSessions(Long userId, Long recordId) {
        Record record = getOwnedRecord(userId, recordId);

        return sessionRepository.findAllByRecord_RecordIdOrderByCreatedAtDesc(record.getRecordId())
                .stream()
                .map(thinkingToolMapper::toSessionSummaryResponse)
                .toList();
    }

    public ThinkingToolSessionResponse getThinkingToolSession(Long userId, Long recordId, Long thinkingToolSessionId) {
        getOwnedRecord(userId, recordId);

        ThinkingToolSession session = sessionRepository.findByThinkingToolSessionIdAndRecord_RecordId(thinkingToolSessionId, recordId)
                .orElseThrow(() -> new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_SESSION_NOT_FOUND));

        List<ThinkingToolAnswer> answers = answerRepository.findAllByThinkingToolSession_ThinkingToolSessionId(session.getThinkingToolSessionId());

        return thinkingToolMapper.toSessionResponse(session, answers);
    }

    private void validateAnswerRequests(List<ThinkingToolAnswerRequest> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
        }

        Set<Long> distinctQuestionIds = new HashSet<>();

        for (ThinkingToolAnswerRequest answer : answers) {
            if (!distinctQuestionIds.add(answer.questionId())) {
                throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_DUPLICATE_QUESTION);
            }
        }
    }

    private void validateQuestionsBelongToTool(List<ThinkingToolQuestion> questions, ThinkingToolCode toolCode) {
        boolean hasInvalidQuestion = questions.stream()
                .anyMatch(question -> question.getToolCode() != toolCode);

        if (hasInvalidQuestion) {
            throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
        }
    }

    private void validateRequiredQuestions(List<ThinkingToolQuestion> requiredQuestions, List<ThinkingToolAnswerRequest> answers) {
        Set<Long> answeredQuestionIds = answers.stream()
                .map(ThinkingToolAnswerRequest::questionId)
                .collect(Collectors.toSet());

        for (ThinkingToolQuestion question : requiredQuestions) {
            if (!answeredQuestionIds.contains(question.getQuestionId())) {
                throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_REQUIRED_ANSWER_MISSING);
            }
        }
    }

    private void validateAnswerByInputType(
            Map<Long, ThinkingToolQuestion> questionMap,
            List<ThinkingToolAnswerRequest> answers
    ) {
        for (ThinkingToolAnswerRequest answer : answers) {
            ThinkingToolQuestion question = questionMap.get(answer.questionId());

            if (question == null) {
                throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_QUESTION_NOT_FOUND);
            }

            validateSingleAnswer(question, answer);
        }
    }

    private void validateSingleAnswer(ThinkingToolQuestion question, ThinkingToolAnswerRequest answer) {
        QuestionInputType inputType = question.getInputType();

        switch (inputType) {
            case TEXT, CHECKBOX -> {
                if (answer.answerValue() != null) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
                }

                if (question.isRequired() && isBlank(answer.answerText())) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_REQUIRED_ANSWER_MISSING);
                }
            }

            case SLIDER -> {
                if (!isBlank(answer.answerText())) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
                }

                if (question.isRequired() && answer.answerValue() == null) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_REQUIRED_ANSWER_MISSING);
                }

                if (answer.answerValue() != null && (answer.answerValue() < 0 || answer.answerValue() > 100)) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
                }
            }

            case GUIDE -> {
                if (!isBlank(answer.answerText()) || answer.answerValue() != null) {
                    throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_INVALID_REQUEST);
                }
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private Record getOwnedRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ThinkingToolErrorCode.THINKING_TOOL_RECORD_NOT_OWNED);
        }

        return record;
    }
}