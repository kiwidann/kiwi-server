package com.kiwi.kiwiserver.domain.dailyrecord.cbt.service;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request.CbtAnswerRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request.CbtCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.*;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.exception.CbtErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.mapper.CbtMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository.*;
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
public class CbtService {

    private final RecordRepository recordRepository;
    private final TagRepository tagRepository;
    private final CbtQuestionRepository questionRepository;
    private final CbtSessionRepository sessionRepository;
    private final CbtAnswerRepository answerRepository;
    private final CbtMapper cbtMapper;

    public List<TagResponse> getTags() {
        return tagRepository.findAllByOrderByNameAsc()
                .stream()
                .map(cbtMapper::toTagResponse)
                .toList();
    }

    public List<CbtQuestionResponse> getQuestions() {
        return questionRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(cbtMapper::toQuestionResponse)
                .toList();
    }

    @Transactional
    public CbtSessionResponse createCbt(Long userId, Long recordId, CbtCreateRequest request) {
        Record record = getOwnedRecord(userId, recordId);

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new BusinessException(CbtErrorCode.CBT_TAG_NOT_FOUND));

        validateAnswerRequests(request.answers());

        List<Long> questionIds = request.answers().stream()
                .map(CbtAnswerRequest::questionId)
                .toList();

        List<CbtQuestion> questions = questionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new BusinessException(CbtErrorCode.CBT_QUESTION_NOT_FOUND);
        }

        Map<Long, CbtQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(CbtQuestion::getQuestionId, Function.identity()));

        validateRequiredQuestions(questions, request.answers());

        CbtSession session = CbtSession.builder()
                .record(record)
                .tag(tag)
                .beforeEmotionScore(request.beforeEmotionScore())
                .afterEmotionScore(request.afterEmotionScore())
                .build();

        CbtSession savedSession = sessionRepository.save(session);

        List<CbtAnswer> answers = request.answers().stream()
                .map(answerRequest -> new CbtAnswer(
                        savedSession,
                        questionMap.get(answerRequest.questionId()),
                        answerRequest.answerText()
                ))
                .toList();

        List<CbtAnswer> savedAnswers = answerRepository.saveAll(answers);

        return cbtMapper.toSessionResponse(savedSession, savedAnswers);
    }

    public List<CbtSessionSummaryResponse> getCbtSessions(Long userId, Long recordId) {
        Record record = getOwnedRecord(userId, recordId);

        return sessionRepository.findAllByRecord_RecordIdOrderByCreatedAtDesc(record.getRecordId())
                .stream()
                .map(cbtMapper::toSessionSummaryResponse)
                .toList();
    }

    public CbtSessionResponse getCbtSession(Long userId, Long recordId, Long cbtSessionId) {
        getOwnedRecord(userId, recordId);

        CbtSession session = sessionRepository.findByCbtSessionIdAndRecord_RecordId(cbtSessionId, recordId)
                .orElseThrow(() -> new BusinessException(CbtErrorCode.CBT_SESSION_NOT_FOUND));

        List<CbtAnswer> answers = answerRepository.findAllByCbtSession_CbtSessionId(session.getCbtSessionId());

        return cbtMapper.toSessionResponse(session, answers);
    }

    private void validateAnswerRequests(List<CbtAnswerRequest> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new BusinessException(CbtErrorCode.CBT_INVALID_REQUEST);
        }

        Set<Long> distinctQuestionIds = new HashSet<>();
        for (CbtAnswerRequest answer : answers) {
            if (!distinctQuestionIds.add(answer.questionId())) {
                throw new BusinessException(CbtErrorCode.CBT_DUPLICATE_QUESTION);
            }
        }
    }

    private void validateRequiredQuestions(List<CbtQuestion> questions, List<CbtAnswerRequest> answers) {
        Map<Long, String> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        CbtAnswerRequest::questionId,
                        answer -> answer.answerText() == null ? "" : answer.answerText()
                ));

        for (CbtQuestion question : questions) {
            if (question.isRequired()) {
                String answerText = answerMap.get(question.getQuestionId());
                if (answerText == null || answerText.isBlank()) {
                    throw new BusinessException(CbtErrorCode.CBT_REQUIRED_ANSWER_MISSING);
                }
            }
        }
    }

    private Record getOwnedRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new BusinessException(CbtErrorCode.CBT_RECORD_NOT_OWNED);
        }

        return record;
    }
}