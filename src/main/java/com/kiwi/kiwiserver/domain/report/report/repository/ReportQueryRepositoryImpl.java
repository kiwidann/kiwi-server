package com.kiwi.kiwiserver.domain.report.report.repository;

import com.kiwi.kiwiserver.domain.report.report.dto.response.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepositoryImpl implements ReportQueryRepository {

    private final EntityManager em;

    @Override
    public double findAverageEmotionScore(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(r.moodScore)
                from Record r
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public Integer findLowestEmotionScore(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select min(r.moodScore)
                from Record r
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                """, Integer.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }

    @Override
    public Integer findHighestEmotionScore(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select max(r.moodScore)
                from Record r
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                """, Integer.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }

    @Override
    public long countRecords(Long userId, LocalDate from, LocalDate to) {
        Long result = em.createQuery("""
                select count(r)
                from Record r
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                """, Long.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0L : result;
    }

    @Override
    public List<EmotionTrendPointResponse> findEmotionTrend(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.report.dto.response.EmotionTrendPointResponse(
                    r.recordDate,
                    r.moodScore
                )
                from Record r
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                order by r.recordDate asc
                """, EmotionTrendPointResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<KeywordStatResponse> findTopKeywords(Long userId, LocalDate from, LocalDate to, int limit) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.report.dto.response.KeywordStatResponse(
                    k.name,
                    count(k),
                    avg(r.moodScore)
                )
                from RecordKeyword rk
                join rk.record r
                join rk.keyword k
                where r.user.userId = :userId
                  and r.recordDate between :from and :to
                group by k.name
                order by count(k) desc
                """, KeywordStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countThinkingToolSessions(Long userId, LocalDate from, LocalDate to) {
        Long result = em.createQuery("""
                select count(s)
                from ThinkingToolSession s
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                """, Long.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0L : result;
    }

    @Override
    public double findAverageThinkingToolBeforeScore(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(s.beforeEmotionScore)
                from ThinkingToolSession s
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public double findAverageThinkingToolAfterScore(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(s.afterEmotionScore)
                from ThinkingToolSession s
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public double findAverageThinkingToolImprovement(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(s.afterEmotionScore - s.beforeEmotionScore)
                from ThinkingToolSession s
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public List<ThinkingToolTagStatResponse> findThinkingToolTagStats(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.report.dto.response.ThinkingToolTagStatResponse(
                    t.name,
                    count(s),
                    avg(s.afterEmotionScore - s.beforeEmotionScore)
                )
                from ThinkingToolSession s
                join s.tag t
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                group by t.name
                order by count(s) desc
                """, ThinkingToolTagStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<ThinkingToolSessionStatResponse> findThinkingToolSessionStats(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.report.dto.response.ThinkingToolSessionStatResponse(
                    s.thinkingToolSessionId,
                    s.record.recordDate,
                    s.beforeEmotionScore,
                    s.afterEmotionScore,
                    (s.afterEmotionScore - s.beforeEmotionScore),
                    t.name
                )
                from ThinkingToolSession s
                join s.tag t
                where s.record.user.userId = :userId
                  and s.record.recordDate between :from and :to
                order by s.record.recordDate asc, s.thinkingToolSessionId asc
                """, ThinkingToolSessionStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<ThinkingToolStatResponse> findThinkingToolStats(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
            select new com.kiwi.kiwiserver.domain.report.report.dto.response.ThinkingToolStatResponse(
                s.toolCode,
                count(s),
                avg(s.afterEmotionScore - s.beforeEmotionScore)
            )
            from ThinkingToolSession s
            where s.record.user.userId = :userId
              and s.record.recordDate between :from and :to
            group by s.toolCode
            order by count(s) desc
            """, ThinkingToolStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<ThinkingToolStatResponse> findTopThinkingTools(Long userId, LocalDate from, LocalDate to, int limit) {
        return em.createQuery("""
            select new com.kiwi.kiwiserver.domain.report.report.dto.response.ThinkingToolStatResponse(
                s.toolCode,
                count(s),
                avg(s.afterEmotionScore - s.beforeEmotionScore)
            )
            from ThinkingToolSession s
            where s.record.user.userId = :userId
              and s.record.recordDate between :from and :to
            group by s.toolCode
            order by count(s) desc
            """, ThinkingToolStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .setMaxResults(limit)
                .getResultList();
    }
}