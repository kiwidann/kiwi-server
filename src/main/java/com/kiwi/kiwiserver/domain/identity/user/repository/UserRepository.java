package com.kiwi.kiwiserver.domain.identity.user.repository;

import java.util.Optional;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccount_AccountId(Long accountId);

    Optional<User> findByUserId(Long userId);

    boolean existsByNickname(String nickname);
}
