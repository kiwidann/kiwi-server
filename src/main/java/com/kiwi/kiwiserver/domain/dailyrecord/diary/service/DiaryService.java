package com.kiwi.kiwiserver.domain.dailyrecord.diary.service;

import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.request.DiaryCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.request.DiaryUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.entity.Diary;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.exception.DiaryErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.mapper.DiaryMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.repository.DiaryRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import com.kiwi.kiwiserver.domain.dailyrecord.record.exception.RecordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.record.repository.RecordRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final RecordRepository recordRepository;
    private final DiaryMapper diaryMapper;

    @Transactional
    public DiaryResponse createDiary(Long userId, DiaryCreateRequest request) {
        Record record = getOwnedRecord(userId, request.recordId());

        Diary diary = Diary.builder()
                .record(record)
                .title(request.title())
                .content(request.content())
                .build();

        Diary savedDiary = diaryRepository.save(diary);
        return diaryMapper.toResponse(savedDiary);
    }

    public List<DiaryResponse> getDiaries(Long userId, Long recordId) {
        getOwnedRecord(userId, recordId);

        return diaryRepository.findAllByRecord_RecordIdAndIsDeletedFalseOrderByCreatedAtAsc(recordId)
                .stream()
                .map(diaryMapper::toResponse)
                .toList();
    }

    public DiaryResponse getDiary(Long userId, Long recordId, Long diaryId) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByDiaryIdAndRecord_RecordIdAndIsDeletedFalse(diaryId, recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        return diaryMapper.toResponse(diary);
    }

    @Transactional
    public DiaryResponse updateDiary(Long userId, Long recordId, Long diaryId, DiaryUpdateRequest request) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByDiaryIdAndRecord_RecordIdAndIsDeletedFalse(diaryId, recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        diary.update(request.title(), request.content());

        return diaryMapper.toResponse(diary);
    }

    @Transactional
    public void deleteDiary(Long userId, Long recordId, Long diaryId) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByDiaryIdAndRecord_RecordIdAndIsDeletedFalse(diaryId, recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        diary.softDelete();
    }

    private Record getOwnedRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new BusinessException(DiaryErrorCode.RECORD_NOT_OWNED);
        }

        return record;
    }
}