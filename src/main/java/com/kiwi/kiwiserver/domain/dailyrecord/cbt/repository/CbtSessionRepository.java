package com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtSession;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CbtSessionRepository extends JpaRepository<CbtSession, Long> {

    List<CbtSession> findAllByRecord_RecordIdOrderByCreatedAtDesc(Long recordId);

    List<CbtSession> findAllByRecord_RecordIdAndToolCodeOrderByCreatedAtDesc(
            Long recordId,
            ThinkingToolCode toolCode
    );

    Optional<CbtSession> findByCbtSessionIdAndRecord_RecordId(
            Long cbtSessionId,
            Long recordId
    );
}