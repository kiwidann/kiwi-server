package com.kiwi.kiwiserver.domain.dailyrecord.record.service;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response.CbtSessionSummaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtSession;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.mapper.CbtMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository.CbtSessionRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.entity.Diary;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.mapper.DiaryMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.repository.DiaryRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.RecordKeyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.mapper.KeywordMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository.RecordKeywordRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordDetailResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import com.kiwi.kiwiserver.domain.dailyrecord.record.exception.RecordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.record.mapper.RecordMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.record.repository.RecordRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordDetailService {

    private final RecordRepository recordRepository;
    private final DiaryRepository diaryRepository;
    private final RecordKeywordRepository recordKeywordRepository;
    private final CbtSessionRepository cbtSessionRepository;

    private final RecordMapper recordMapper;
    private final DiaryMapper diaryMapper;
    private final KeywordMapper keywordMapper;
    private final CbtMapper cbtMapper;

    public RecordDetailResponse getRecordDetail(Long userId, Long recordId) {
        Record record = getOwnedRecord(userId, recordId);

        DiaryResponse diaryResponse = diaryRepository.findByRecord_RecordId(recordId)
                .map(diaryMapper::toResponse)
                .orElse(null);

        List<KeywordResponse> keywordResponses = recordKeywordRepository.findAllByRecord_RecordId(recordId)
                .stream()
                .map(RecordKeyword::getKeyword)
                .map(keywordMapper::toResponse)
                .toList();

        List<CbtSessionSummaryResponse> cbtSessionResponses = cbtSessionRepository
                .findAllByRecord_RecordIdOrderByCreatedAtDesc(recordId)
                .stream()
                .map(cbtMapper::toSessionSummaryResponse)
                .toList();

        return recordMapper.toDetailResponse(
                record,
                diaryResponse,
                keywordResponses,
                cbtSessionResponses
        );
    }

    private Record getOwnedRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new BusinessException(RecordErrorCode.RECORD_NOT_OWNED);
        }

        return record;
    }
}