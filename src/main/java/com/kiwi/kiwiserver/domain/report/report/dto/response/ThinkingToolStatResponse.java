package com.kiwi.kiwiserver.domain.report.report.dto.response;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;
import lombok.Getter;

@Getter
public class ThinkingToolStatResponse {

    private final ThinkingToolCode toolCode;
    private final String toolName;
    private final long count;
    private final double averageImprovement;

    public ThinkingToolStatResponse(
            ThinkingToolCode toolCode,
            long count,
            Double averageImprovement
    ) {
        this.toolCode = toolCode;
        this.toolName = toToolName(toolCode);
        this.count = count;
        this.averageImprovement = averageImprovement == null ? 0.0 : averageImprovement;
    }

    private String toToolName(ThinkingToolCode toolCode) {
        return switch (toolCode) {
            case RETHINK_THOUGHT -> "생각 다시 보기";
            case NEW_PERSPECTIVE -> "다른 시선에서 바라보기";
            case EMOTION_FACT -> "느낌과 사실 나누기";
            case WORST_THOUGHT -> "최악의 생각 살펴보기";
            case CALM_MIND -> "마음 쉬어가기";
            case BREATH_STABILIZE -> "호흡하고 안정 찾기";
        };
    }
}