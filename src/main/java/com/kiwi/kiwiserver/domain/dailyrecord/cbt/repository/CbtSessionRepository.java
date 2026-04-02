package com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CbtSessionRepository extends JpaRepository<CbtSession, Long> {

    List<CbtSession> findAllByRecord_RecordIdOrderByCreatedAtDesc(Long recordId);

    Optional<CbtSession> findByCbtSessionIdAndRecord_RecordId(Long cbtSessionId, Long recordId);
}