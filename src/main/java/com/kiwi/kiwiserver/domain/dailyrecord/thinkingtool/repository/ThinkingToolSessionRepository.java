package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolSession;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThinkingToolSessionRepository extends JpaRepository<ThinkingToolSession, Long> {

    List<ThinkingToolSession> findAllByRecord_RecordIdOrderByCreatedAtDesc(Long recordId);

    List<ThinkingToolSession> findAllByRecord_RecordIdAndToolCodeOrderByCreatedAtDesc(
            Long recordId,
            ThinkingToolCode toolCode
    );

    Optional<ThinkingToolSession> findByThinkingToolSessionIdAndRecord_RecordId(
            Long thinkingToolSessionId,
            Long recordId
    );
}