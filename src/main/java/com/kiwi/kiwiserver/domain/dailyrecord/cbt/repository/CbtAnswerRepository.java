package com.kiwi.kiwiserver.domain.dailyrecord.cbt.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity.CbtAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CbtAnswerRepository extends JpaRepository<CbtAnswer, Long> {

    List<CbtAnswer> findAllByCbtSession_CbtSessionId(Long sessionId);
}