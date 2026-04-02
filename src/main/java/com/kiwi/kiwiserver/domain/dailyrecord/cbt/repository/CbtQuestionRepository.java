package com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CbtQuestionRepository extends JpaRepository<CbtQuestion, Long> {

    List<CbtQuestion> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}