package com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findAllByUser_UserIdOrderByKeywordIdAsc(Long userId);

    boolean existsByUser_UserIdAndName(Long userId, String name);

    Optional<Keyword> findByKeywordIdAndUser_UserId(Long keywordId, Long userId);

    List<Keyword> findAllByKeywordIdInAndUser_UserId(List<Long> keywordIds, Long userId);
}