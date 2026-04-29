package com.kiwi.kiwiserver.domain.report.report.repository;

import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtSessionStatResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtTagStatResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.EmotionTrendPointResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.KeywordStatResponse;
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
                select new com.kiwi.kiwiserver.domain.report.dto.response.EmotionTrendPointResponse(
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
                select new com.kiwi.kiwiserver.domain.report.dto.response.KeywordStatResponse(
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
    public long countCbtSessions(Long userId, LocalDate from, LocalDate to) {
        Long result = em.createQuery("""
                select count(c)
                from CbtSession c
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                """, Long.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0L : result;
    }

    @Override
    public double findAverageCbtBeforeScore(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(c.beforeEmotionScore)
                from CbtSession c
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public double findAverageCbtAfterScore(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(c.afterEmotionScore)
                from CbtSession c
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public double findAverageCbtImprovement(Long userId, LocalDate from, LocalDate to) {
        Double result = em.createQuery("""
                select avg(c.afterEmotionScore - c.beforeEmotionScore)
                from CbtSession c
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                """, Double.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();

        return result == null ? 0.0 : result;
    }

    @Override
    public List<CbtTagStatResponse> findCbtTagStats(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.dto.response.CbtTagStatResponse(
                    t.name,
                    count(c),
                    avg(c.afterEmotionScore - c.beforeEmotionScore)
                )
                from CbtSession c
                join c.tag t
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                group by t.name
                order by count(c) desc
                """, CbtTagStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<CbtSessionStatResponse> findCbtSessionStats(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("""
                select new com.kiwi.kiwiserver.domain.report.dto.response.CbtSessionStatResponse(
                    c.cbtSessionId,
                    c.record.recordDate,
                    c.beforeEmotionScore,
                    c.afterEmotionScore,
                    (c.afterEmotionScore - c.beforeEmotionScore),
                    t.name
                )
                from CbtSession c
                join c.tag t
                where c.record.user.userId = :userId
                  and c.record.recordDate between :from and :to
                order by c.record.recordDate asc, c.cbtSessionId asc
                """, CbtSessionStatResponse.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}