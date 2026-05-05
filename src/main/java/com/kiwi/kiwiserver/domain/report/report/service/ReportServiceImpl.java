package com.kiwi.kiwiserver.domain.report.report.service;

import com.kiwi.kiwiserver.domain.report.report.dto.response.*;
import com.kiwi.kiwiserver.domain.report.report.repository.ReportQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ReportQueryRepository reportQueryRepository;

    @Override
    public ReportDashboardResponse getDashboard(Long userId, LocalDate from, LocalDate to) {
        double averageEmotionScore = reportQueryRepository.findAverageEmotionScore(userId, from, to);
        Integer lowestEmotionScore = reportQueryRepository.findLowestEmotionScore(userId, from, to);
        Integer highestEmotionScore = reportQueryRepository.findHighestEmotionScore(userId, from, to);
        long recordCount = reportQueryRepository.countRecords(userId, from, to);

        var topKeywords = reportQueryRepository.findTopKeywords(userId, from, to, 3);

        long thinkingToolCount = reportQueryRepository.countThinkingToolSessions(userId, from, to);
        double averageBeforeScore = reportQueryRepository.findAverageThinkingToolBeforeScore(userId, from, to);
        double averageAfterScore = reportQueryRepository.findAverageThinkingToolAfterScore(userId, from, to);
        double averageImprovement = reportQueryRepository.findAverageThinkingToolImprovement(userId, from, to);

        var topThinkingTools = reportQueryRepository.findTopThinkingTools(userId, from, to, 3);

        List<String> insights = createInsights(
                averageEmotionScore,
                topKeywords.stream().map(k -> k.getKeyword()).toList(),
                thinkingToolCount,
                averageImprovement,
                topThinkingTools
        );

        return ReportDashboardResponse.builder()
                .from(from)
                .to(to)
                .recordCount(recordCount)
                .averageEmotionScore(averageEmotionScore)
                .lowestEmotionScore(lowestEmotionScore)
                .highestEmotionScore(highestEmotionScore)
                .topKeywords(topKeywords)
                .thinkingToolCount(thinkingToolCount)
                .averageThinkingToolBeforeScore(averageBeforeScore)
                .averageThinkingToolAfterScore(averageAfterScore)
                .averageThinkingToolImprovement(averageImprovement)
                .topThinkingTools(topThinkingTools)
                .insights(insights)
                .build();
    }

    @Override
    public EmotionTrendResponse getEmotionTrend(Long userId, LocalDate from, LocalDate to) {
        var points = reportQueryRepository.findEmotionTrend(userId, from, to);
        double averageEmotionScore = reportQueryRepository.findAverageEmotionScore(userId, from, to);
        Integer lowestEmotionScore = reportQueryRepository.findLowestEmotionScore(userId, from, to);
        Integer highestEmotionScore = reportQueryRepository.findHighestEmotionScore(userId, from, to);

        return EmotionTrendResponse.builder()
                .from(from)
                .to(to)
                .averageEmotionScore(averageEmotionScore)
                .lowestEmotionScore(lowestEmotionScore)
                .highestEmotionScore(highestEmotionScore)
                .points(points)
                .build();
    }

    @Override
    public KeywordReportResponse getKeywordReport(Long userId, LocalDate from, LocalDate to, int limit) {
        var keywords = reportQueryRepository.findTopKeywords(userId, from, to, limit);

        return KeywordReportResponse.builder()
                .from(from)
                .to(to)
                .keywords(keywords)
                .build();
    }

    @Override
    public ThinkingToolReportResponse getThinkingToolReport(Long userId, LocalDate from, LocalDate to) {
        long thinkingToolCount = reportQueryRepository.countThinkingToolSessions(userId, from, to);
        double averageBeforeScore = reportQueryRepository.findAverageThinkingToolBeforeScore(userId, from, to);
        double averageAfterScore = reportQueryRepository.findAverageThinkingToolAfterScore(userId, from, to);
        double averageImprovement = reportQueryRepository.findAverageThinkingToolImprovement(userId, from, to);

        var toolStats = reportQueryRepository.findThinkingToolStats(userId, from, to);
        var tagStats = reportQueryRepository.findThinkingToolTagStats(userId, from, to);
        var sessionStats = reportQueryRepository.findThinkingToolSessionStats(userId, from, to);

        return ThinkingToolReportResponse.builder()
                .from(from)
                .to(to)
                .thinkingToolCount(thinkingToolCount)
                .averageBeforeScore(averageBeforeScore)
                .averageAfterScore(averageAfterScore)
                .averageImprovement(averageImprovement)
                .toolStats(toolStats)
                .tagStats(tagStats)
                .sessionStats(sessionStats)
                .build();
    }

    private List<String> createInsights(
            double averageEmotionScore,
            List<String> topKeywords,
            long thinkingToolCount,
            double averageImprovement,
            List<ThinkingToolStatResponse> topThinkingTools
    ) {
        List<String> insights = new ArrayList<>();

        if (averageEmotionScore <= 4.0) {
            insights.add("최근 감정 점수가 전반적으로 낮은 편이에요");
        }

        if (!topKeywords.isEmpty()) {
            insights.add("자주 기록된 감정 키워드는 " + String.join(", ", topKeywords) + " 이에요");
        }

        if (thinkingToolCount > 0 && averageImprovement > 0) {
            insights.add("생각정리도구 사용 이후 감정 점수가 평균적으로 상승하는 경향이 보여요");
        }

        if (!topThinkingTools.isEmpty()) {
            insights.add("최근 가장 많이 사용한 생각정리도구는 '" + topThinkingTools.get(0).getToolName() + "'예요");
        }

        if (insights.isEmpty()) {
            insights.add("현재 기간에 대한 리포트 데이터가 충분하지 않아요");
        }

        return insights;
    }
}