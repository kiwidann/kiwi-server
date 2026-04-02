package com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KeywordUpdateRequest(
        @NotBlank(message = "키워드 이름은 필수입니다")
        @Size(max = 50)
        String name
) {}