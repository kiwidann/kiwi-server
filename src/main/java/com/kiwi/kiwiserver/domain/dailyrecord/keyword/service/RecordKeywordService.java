package com.kiwi.kiwiserver.domain.dailyrecord.keyword.service;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.RecordKeywordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.Keyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.RecordKeyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.exception.KeywordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.mapper.KeywordMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository.KeywordRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository.RecordKeywordRepository;
import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import com.kiwi.kiwiserver.domain.dailyrecord.record.exception.RecordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.record.repository.RecordRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordKeywordService {

    private final RecordRepository recordRepository;
    private final KeywordRepository keywordRepository;
    private final RecordKeywordRepository recordKeywordRepository;
    private final KeywordMapper keywordMapper;

    public List<KeywordResponse> getRecordKeywords(Long userId, Long recordId) {
        Record record = getOwnedRecord(userId, recordId);

        return recordKeywordRepository.findAllByRecord_RecordId(record.getRecordId())
                .stream()
                .map(RecordKeyword::getKeyword)
                .map(keywordMapper::toResponse)
                .toList();
    }

    /**
     * 특정 record에 연결된 키워드 목록을 요청 값으로 "완전히 교체".
     *
     * 동작 방식:
     * 1. record가 현재 사용자 소유인지 검증
     * 2. 요청으로 들어온 keywordIds를 정리 (null 처리 + 중복 제거)
     * 3. 해당 keywordIds가 모두 현재 사용자 소유인지 검증
     * 4. 기존 record-keyword 연결 전부 삭제
     * 5. 요청 키워드로 새로운 연결 생성 및 저장
     *
     * 즉, 부분 추가/삭제가 아니라 "전체 덮어쓰기(replace)" 방식.
     */
    @Transactional
    public List<KeywordResponse> replaceRecordKeywords(Long userId, Long recordId, RecordKeywordUpdateRequest request) {
        // 1. 해당 record가 존재하고, 현재 사용자 소유인지 검증
        Record record = getOwnedRecord(userId, recordId);

        // 2. 요청 keywordIds null 방지 (null이면 빈 리스트로 처리)
        List<Long> requestedKeywordIds = request.keywordIds() == null ? List.of() : request.keywordIds();

        // 3. 중복 제거 (예: [1,2,2,3] → [1,2,3])
        // LinkedHashSet을 사용해 순서도 유지
        Set<Long> distinctKeywordIds = new LinkedHashSet<>(requestedKeywordIds);

        // 4. DB에서 실제 키워드 조회 (현재 사용자 소유 키워드만 조회)
        List<Keyword> keywords = keywordRepository.findAllByKeywordIdInAndUser_UserId(
                distinctKeywordIds.stream().toList(),
                userId
        );

        // 5. 요청 개수와 조회된 개수가 다르면
        // → 존재하지 않거나, 다른 사용자의 키워드 포함된 경우
        if (keywords.size() != distinctKeywordIds.size()) {
            throw new BusinessException(KeywordErrorCode.KEYWORD_NOT_FOUND);
        }

        // 6. 기존 record에 연결된 모든 키워드 삭제: 완전 교체 방식이므로 전부 지우고 다시 넣음
        recordKeywordRepository.deleteAllByRecord_RecordId(record.getRecordId());

        // 7. 새로운 record-keyword 연결 객체 생성
        List<RecordKeyword> recordKeywords = keywords.stream()
                .map(keyword -> new RecordKeyword(record, keyword))
                .toList();

        // 8. 새 연결을 DB에 저장
        recordKeywordRepository.saveAll(recordKeywords);

        // 9. 최종적으로 연결된 키워드 목록을 응답으로 반환
        return keywords.stream()
                .map(keywordMapper::toResponse)
                .toList();
    }

    private Record getOwnedRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(RecordErrorCode.RECORD_NOT_FOUND));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new BusinessException(KeywordErrorCode.KEYWORD_RECORD_NOT_OWNED);
        }

        return record;
    }
}