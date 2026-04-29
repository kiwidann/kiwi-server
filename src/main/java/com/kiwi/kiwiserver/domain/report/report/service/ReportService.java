package com.kiwi.kiwiserver.domain.report.report.service;

import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtReportResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.EmotionTrendResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.KeywordReportResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.ReportDashboardResponse;

import java.time.LocalDate;

public interface ReportService {

    ReportDashboardResponse getDashboard(Long userId, LocalDate from, LocalDate to);

    EmotionTrendResponse getEmotionTrend(Long userId, LocalDate from, LocalDate to);

    KeywordReportResponse getKeywordReport(Long userId, LocalDate from, LocalDate to, int limit);

    CbtReportResponse getCbtReport(Long userId, LocalDate from, LocalDate to);
}