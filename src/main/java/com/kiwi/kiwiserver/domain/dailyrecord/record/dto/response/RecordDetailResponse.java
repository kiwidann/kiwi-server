package com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response.CbtSessionSummaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record RecordDetailResponse(
        Long recordId,
        LocalDate recordDate,
        Integer moodScore,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        DiaryResponse diary,
        List<KeywordResponse> keywords,
        List<CbtSessionSummaryResponse> cbtSessions
) {
}