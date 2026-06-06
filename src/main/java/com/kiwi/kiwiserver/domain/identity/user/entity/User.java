package com.kiwi.kiwiserver.domain.identity.user.entity;

import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(name = "kiwi_balance", nullable = false)
    private Integer kiwiBalance;

    @Builder
    private User(
            Account account,
            String nickname,
            String profileImageUrl,
            Integer kiwiBalance
    ) {
        this.account = account;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.kiwiBalance = kiwiBalance;
    }

    public static User create(Account account, String nickname, String profileImageUrl) {
        return User.builder()
                .account(account)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .kiwiBalance(0)
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateKiwiBalance(Integer kiwiBalance) {
        this.kiwiBalance = kiwiBalance;
    }

    public void decreaseKiwiBalance(int amount) {
        this.kiwiBalance -= amount;
    }
}
