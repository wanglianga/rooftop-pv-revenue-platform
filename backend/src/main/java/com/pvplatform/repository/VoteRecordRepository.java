package com.pvplatform.repository;

import com.pvplatform.entity.VoteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {
    List<VoteRecord> findByTopicId(Long topicId);
    boolean existsByTopicIdAndOwnerId(Long topicId, Long ownerId);
}
