package com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity;

import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cbt_answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CbtAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cbt_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_answers_session")
    )
    private CbtSession cbtSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_answers_question")
    )
    private CbtQuestion question;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "answer_value")
    private Integer answerValue;

    public CbtAnswer(
            CbtSession cbtSession,
            CbtQuestion question,
            String answerText,
            Integer answerValue
    ) {
        this.cbtSession = cbtSession;
        this.question = question;
        this.answerText = answerText;
        this.answerValue = answerValue;
    }
}