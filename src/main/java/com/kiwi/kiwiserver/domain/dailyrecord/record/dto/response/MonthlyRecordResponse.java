package com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response;

import java.time.YearMonth;
import java.util.List;

public record MonthlyRecordResponse(
        YearMonth yearMonth,
        List<RecordResponse> records
) {
}