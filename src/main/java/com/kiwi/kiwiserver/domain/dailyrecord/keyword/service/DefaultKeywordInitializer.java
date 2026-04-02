package com.kiwi.kiwiserver.domain.dailyrecord.keyword.service;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.Keyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository.KeywordRepository;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultKeywordInitializer {

    private static final List<String> DEFAULT_KEYWORDS = List.of(
            "행복함",
            "뿌듯함",
            "평온함",
            "설렘",
            "신남",
            "싱숭생숭",
            "불안함",
            "우울함",
            "짜증남",
            "외로움",
            "피곤함"
    );

    private final KeywordRepository keywordRepository;

    public void initialize(User user) {
        List<Keyword> keywords = DEFAULT_KEYWORDS.stream()
                .map(name -> Keyword.builder()
                        .user(user)
                        .name(name)
                        .build())
                .toList();

        keywordRepository.saveAll(keywords);
    }
}