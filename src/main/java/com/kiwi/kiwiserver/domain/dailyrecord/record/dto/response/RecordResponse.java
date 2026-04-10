package com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record RecordResponse(
        Long recordId,
        LocalDate recordDate,
        Integer moodScore,
        Instant createdAt,
        Instant updatedAt
) {
}