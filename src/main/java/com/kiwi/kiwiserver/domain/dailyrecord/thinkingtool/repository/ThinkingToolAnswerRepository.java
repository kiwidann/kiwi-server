package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThinkingToolAnswerRepository extends JpaRepository<ThinkingToolAnswer, Long> {

    List<ThinkingToolAnswer> findAllByThinkingToolSession_ThinkingToolSessionId(Long sessionId);
}