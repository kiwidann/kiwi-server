package com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.global.entity.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "keywords",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_keyword", columnNames = {"user_id", "name"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_keywords_user")
    )
    private User user;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Builder
    private Keyword(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
