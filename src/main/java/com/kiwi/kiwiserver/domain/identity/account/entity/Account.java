package com.kiwi.kiwiserver.domain.identity.account.entity;

import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    @Builder
    private Account(
            String email,
            String passwordHash,
            Boolean isDeleted,
            OffsetDateTime deletedAt,
            Boolean isVerified,
            OffsetDateTime verifiedAt
    ) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.isVerified = isVerified;
        this.verifiedAt = verifiedAt;
    }

    public static Account create(String email, String passwordHash) {
        return Account.builder()
                .email(email)
                .passwordHash(passwordHash)
                .isDeleted(false)
                .deletedAt(null)
                .isVerified(false)
                .verifiedAt(null)
                .build();
    }

    public void updatePasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void softDelete(OffsetDateTime deletedAt) {
        this.isDeleted = true;
        this.deletedAt = deletedAt;
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    public void verify(OffsetDateTime verifiedAt) {
        this.isVerified = true;
        this.verifiedAt = verifiedAt;
    }
}