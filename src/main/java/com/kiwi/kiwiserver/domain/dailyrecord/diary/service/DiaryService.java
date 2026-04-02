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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (diaryRepository.existsByRecord_RecordId(record.getRecordId())) {
            throw new BusinessException(DiaryErrorCode.DIARY_ALREADY_EXISTS);
        }

        Diary diary = Diary.builder()
                .record(record)
                .title(request.title())
                .content(request.content())
                .build();

        Diary savedDiary = diaryRepository.save(diary);
        return diaryMapper.toResponse(savedDiary);
    }

    public DiaryResponse getDiaryByRecord(Long userId, Long recordId) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByRecord_RecordId(recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        return diaryMapper.toResponse(diary);
    }

    public Page<DiaryResponse> getMyDiaries(Long userId, Pageable pageable) {
        return diaryRepository.findAllByRecord_User_UserId(userId, pageable)
                .map(diaryMapper::toResponse);
    }

    @Transactional
    public DiaryResponse updateDiary(Long userId, Long recordId, DiaryUpdateRequest request) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByRecord_RecordId(recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        diary.update(request.title(), request.content());

        return diaryMapper.toResponse(diary);
    }

    @Transactional
    public void deleteDiary(Long userId, Long recordId) {
        getOwnedRecord(userId, recordId);

        Diary diary = diaryRepository.findByRecord_RecordId(recordId)
                .orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

        diaryRepository.delete(diary);
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