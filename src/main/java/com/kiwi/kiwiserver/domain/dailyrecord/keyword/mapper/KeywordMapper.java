package com.kiwi.kiwiserver.domain.dailyrecord.keyword.mapper;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.Keyword;
import org.springframework.stereotype.Component;

@Component
public class KeywordMapper {

    public KeywordResponse toResponse(Keyword keyword) {
        return new KeywordResponse(
                keyword.getKeywordId(),
                keyword.getName()
        );
    }
}