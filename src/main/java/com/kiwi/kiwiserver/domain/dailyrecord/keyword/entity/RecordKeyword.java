package com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity;

import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "record_keywords")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordKeyword {

    @EmbeddedId
    private RecordKeywordId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("recordId")
    @JoinColumn(
            name = "record_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_rk_record")
    )
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("keywordId")
    @JoinColumn(
            name = "keyword_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_rk_keyword")
    )
    private Keyword keyword;

    public RecordKeyword(Record record, Keyword keyword) {
        this.id = new RecordKeywordId(record.getRecordId(), keyword.getKeywordId());
        this.record = record;
        this.keyword = keyword;
    }
}