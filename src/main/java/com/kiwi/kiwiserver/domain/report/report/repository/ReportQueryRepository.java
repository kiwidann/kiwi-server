package com.kiwi.kiwiserver.domain.report.report.repository;

import com.kiwi.kiwiserver.domain.report.report.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ReportQueryRepository {

    double findAverageEmotionScore(Long userId, LocalDate from, LocalDate to);

    Integer findLowestEmotionScore(Long userId, LocalDate from, LocalDate to);

    Integer findHighestEmotionScore(Long userId, LocalDate from, LocalDate to);

    long countRecords(Long userId, LocalDate from, LocalDate to);

    List<EmotionTrendPointResponse> findEmotionTrend(Long userId, LocalDate from, LocalDate to);

    List<KeywordStatResponse> findTopKeywords(Long userId, LocalDate from, LocalDate to, int limit);

    long countThinkingToolSessions(Long userId, LocalDate from, LocalDate to);

    double findAverageThinkingToolBeforeScore(Long userId, LocalDate from, LocalDate to);

    double findAverageThinkingToolAfterScore(Long userId, LocalDate from, LocalDate to);

    double findAverageThinkingToolImprovement(Long userId, LocalDate from, LocalDate to);

    List<ThinkingToolTagStatResponse> findThinkingToolTagStats(Long userId, LocalDate from, LocalDate to);

    List<ThinkingToolSessionStatResponse> findThinkingToolSessionStats(Long userId, LocalDate from, LocalDate to);

    List<ThinkingToolStatResponse> findThinkingToolStats(Long userId, LocalDate from, LocalDate to);

    List<ThinkingToolStatResponse> findTopThinkingTools(Long userId, LocalDate from, LocalDate to, int limit);
}