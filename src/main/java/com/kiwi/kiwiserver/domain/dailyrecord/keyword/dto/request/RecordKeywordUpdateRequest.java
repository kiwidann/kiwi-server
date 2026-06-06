package com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecordKeywordUpdateRequest(

        @NotNull(message = "키워드 목록은 필수입니다")
        List<Long> keywordIds
) {
}