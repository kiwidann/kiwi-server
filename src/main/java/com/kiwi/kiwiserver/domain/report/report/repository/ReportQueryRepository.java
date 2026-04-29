package com.kiwi.kiwiserver.domain.report.report.repository;

import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtSessionStatResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtTagStatResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.EmotionTrendPointResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.KeywordStatResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportQueryRepository {

    double findAverageEmotionScore(Long userId, LocalDate from, LocalDate to);

    Integer findLowestEmotionScore(Long userId, LocalDate from, LocalDate to);

    Integer findHighestEmotionScore(Long userId, LocalDate from, LocalDate to);

    long countRecords(Long userId, LocalDate from, LocalDate to);

    List<EmotionTrendPointResponse> findEmotionTrend(Long userId, LocalDate from, LocalDate to);

    List<KeywordStatResponse> findTopKeywords(Long userId, LocalDate from, LocalDate to, int limit);

    long countCbtSessions(Long userId, LocalDate from, LocalDate to);

    double findAverageCbtBeforeScore(Long userId, LocalDate from, LocalDate to);

    double findAverageCbtAfterScore(Long userId, LocalDate from, LocalDate to);

    double findAverageCbtImprovement(Long userId, LocalDate from, LocalDate to);

    List<CbtTagStatResponse> findCbtTagStats(Long userId, LocalDate from, LocalDate to);

    List<CbtSessionStatResponse> findCbtSessionStats(Long userId, LocalDate from, LocalDate to);
}