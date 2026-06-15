package com.pvplatform.service;

import com.pvplatform.entity.ConstructionRecord;
import com.pvplatform.repository.ConstructionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstructionRecordService {

    private ConstructionRecordRepository constructionRecordRepository;

    public ConstructionRecordRepository getConstructionRecordRepository() {
        return constructionRecordRepository;
    }

    @Autowired
    public void setConstructionRecordRepository(ConstructionRecordRepository constructionRecordRepository) {
        this.constructionRecordRepository = constructionRecordRepository;
    }

    public List<ConstructionRecord> listByRoofAreaId(Long roofAreaId) {
        return constructionRecordRepository.findByRoofAreaId(roofAreaId);
    }

    public ConstructionRecord saveRecord(ConstructionRecord record) {
        return constructionRecordRepository.save(record);
    }

    public List<ConstructionRecord> listByStage(String stage) {
        return constructionRecordRepository.findByStage(stage);
    }
}
