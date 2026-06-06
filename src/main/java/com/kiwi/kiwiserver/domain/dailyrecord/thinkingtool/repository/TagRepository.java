package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllByOrderByNameAsc();
}