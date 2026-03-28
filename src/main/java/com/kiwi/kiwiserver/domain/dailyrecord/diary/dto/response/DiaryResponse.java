package com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response;

import java.time.OffsetDateTime;

public record DiaryResponse(
        Long diaryId,
        Long recordId,
        String title,
        String content,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
