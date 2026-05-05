package com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response.ThinkingToolSessionSummaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record RecordDetailResponse(
        Long recordId,
        LocalDate recordDate,
        Integer moodScore,
        Instant createdAt,
        Instant updatedAt,
        DiaryResponse diary,
        List<KeywordResponse> keywords,
        List<ThinkingToolSessionSummaryResponse> thinkingToolSessions
) {
}