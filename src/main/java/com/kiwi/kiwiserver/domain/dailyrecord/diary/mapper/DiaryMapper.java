package com.kiwi.kiwiserver.domain.dailyrecord.diary.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.entity.Diary;
import org.springframework.stereotype.Component;

@Component
public class DiaryMapper {

    public DiaryResponse toResponse(Diary diary) {
        return new DiaryResponse(
                diary.getDiaryId(),
                diary.getRecord().getRecordId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }
}
