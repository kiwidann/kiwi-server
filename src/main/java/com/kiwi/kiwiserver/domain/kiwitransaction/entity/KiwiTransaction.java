package com.kiwi.kiwiserver.domain.kiwitransaction.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "kiwi_transactions")
public class KiwiTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tx_id")
    private Long txId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, columnDefinition = "kiwi_tx_type")
    private KiwiTxType type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Builder
    public KiwiTransaction(User user, int amount, KiwiTxType type, OffsetDateTime createdAt) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }

    public static KiwiTransaction create(User user, int amount, KiwiTxType type) {
        return KiwiTransaction.builder()
                .user(user)
                .amount(amount)
                .type(type)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}