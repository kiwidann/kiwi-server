package com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.RecordKeyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.RecordKeywordId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordKeywordRepository extends JpaRepository<RecordKeyword, RecordKeywordId> {

    List<RecordKeyword> findAllByRecord_RecordId(Long recordId);

    void deleteAllByRecord_RecordId(Long recordId);
}