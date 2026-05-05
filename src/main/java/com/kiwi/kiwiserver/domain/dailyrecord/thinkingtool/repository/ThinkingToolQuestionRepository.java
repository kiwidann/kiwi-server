package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolQuestion;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThinkingToolQuestionRepository extends JpaRepository<ThinkingToolQuestion, Long> {

    List<ThinkingToolQuestion> findAllByToolCodeAndIsActiveTrueOrderByDisplayOrderAsc(
            ThinkingToolCode toolCode
    );

    Optional<ThinkingToolQuestion> findByQuestionIdAndToolCodeAndIsActiveTrue(
            Long questionId,
            ThinkingToolCode toolCode
    );
}