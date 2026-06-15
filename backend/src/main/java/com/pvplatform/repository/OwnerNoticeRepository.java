package com.pvplatform.repository;

import com.pvplatform.entity.OwnerNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerNoticeRepository extends JpaRepository<OwnerNotice, Long> {
    List<OwnerNotice> findByTargetAudience(String targetAudience);
}
