package com.kiwi.kiwiserver.domain.dailyrecord.record.service;

import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.request.RecordCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.request.RecordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.MonthlyRecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.exception.RecordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.record.mapper.RecordMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.record.repository.RecordRepository;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RecordMapper recordMapper;


    @Transactional
    public RecordResponse createRecord(Long userId, RecordCreateRequest request) {
        User user = getUser(userId);

        validateRecordDate(request.recordDate());

        boolean exists = recordRepository.existsByUser_UserIdAndRecordDate(userId, request.recordDate());
        if (exists) {
            throw new BusinessException(RecordErrorCode.RECORD_ALREADY_EXISTS);
        }

        Record record = Record.builder()
                .user(user)
                .recordDate(request.recordDate())
                .moodScore(request.moodScore())
                .build();

        Record savedRecord = recordRepository.save(record);
        return recordMapper.toResponse(savedRecord);
    }

    public RecordResponse getRecordByDate(Long userId, LocalDate recordDate) {
        Record record = recordRepository.findByUser_UserIdAndRecordDate(userId, recordDate)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        return recordMapper.toResponse(record);
    }

    public MonthlyRecordResponse getMonthlyRecords(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<RecordResponse> records = recordRepository
                .findAllByUser_UserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, startDate, endDate)
                .stream()
                .map(recordMapper::toResponse)
                .toList();

        return new MonthlyRecordResponse(yearMonth, records);
    }

    @Transactional
    public RecordResponse updateMoodScore(Long userId, LocalDate recordDate, RecordUpdateRequest request) {
        Record record = recordRepository.findByUser_UserIdAndRecordDate(userId, recordDate)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        record.updateMoodScore(request.moodScore());

        return recordMapper.toResponse(record);
    }

    // 기록 날짜가 허용된 범위(오늘 또는 어제)인지 검증
    private void validateRecordDate(LocalDate recordDate) {
        LocalDate today = LocalDate.now();

        if (recordDate.isAfter(today)) {
            throw new BusinessException(RecordErrorCode.INVALID_RECORD_DATE);
        }

        if (recordDate.isBefore(today.minusDays(1))) {
            throw new BusinessException(RecordErrorCode.INVALID_RECORD_DATE);
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }
}