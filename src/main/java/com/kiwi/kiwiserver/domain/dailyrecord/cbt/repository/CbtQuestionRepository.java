package com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtQuestion;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.ThinkingToolCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CbtQuestionRepository extends JpaRepository<CbtQuestion, Long> {

    List<CbtQuestion> findAllByToolCodeAndIsActiveTrueOrderByDisplayOrderAsc(
            ThinkingToolCode toolCode
    );

    Optional<CbtQuestion> findByQuestionIdAndToolCodeAndIsActiveTrue(
            Long questionId,
            ThinkingToolCode toolCode
    );
}