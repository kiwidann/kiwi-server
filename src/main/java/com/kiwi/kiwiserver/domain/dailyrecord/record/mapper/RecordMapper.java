package com.kiwi.kiwiserver.domain.dailyrecord.record.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response.ThinkingToolSessionSummaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordDetailResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordMapper {

    public RecordResponse toResponse(Record record) {
        return new RecordResponse(
                record.getRecordId(),
                record.getRecordDate(),
                record.getMoodScore(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    public RecordDetailResponse toDetailResponse(
            Record record,
            DiaryResponse diary,
            List<KeywordResponse> keywords,
            List<ThinkingToolSessionSummaryResponse> thinkingToolSessions
    ) {
        return new RecordDetailResponse(
                record.getRecordId(),
                record.getRecordDate(),
                record.getMoodScore(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                diary,
                keywords,
                thinkingToolSessions
        );
    }
}