package com.kiwi.kiwiserver.domain.dailyrecord.diary.entity;

import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "diaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "record_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_diaries_record")
    )
    private Record record;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Builder
    private Diary(Record record, String title, String content) {
        this.record = record;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}