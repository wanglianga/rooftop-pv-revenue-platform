package com.pvplatform.repository;

import com.pvplatform.entity.ConstructionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstructionRecordRepository extends JpaRepository<ConstructionRecord, Long> {
    List<ConstructionRecord> findByRoofAreaId(Long roofAreaId);
    List<ConstructionRecord> findByStage(String stage);
}
