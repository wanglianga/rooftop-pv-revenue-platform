package com.pvplatform.repository;

import com.pvplatform.entity.VoteTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTopicRepository extends JpaRepository<VoteTopic, Long> {
}
