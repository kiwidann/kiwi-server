package com.kiwi.kiwiserver.domain.dailyrecord.record.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import org.springframework.stereotype.Component;

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
}