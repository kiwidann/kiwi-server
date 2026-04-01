package com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordKeywordId implements Serializable {

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "keyword_id")
    private Long keywordId;
}