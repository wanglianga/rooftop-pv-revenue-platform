package com.pvplatform.service;

import com.pvplatform.entity.GridApplication;
import com.pvplatform.repository.GridApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GridApplicationService {

    private GridApplicationRepository gridApplicationRepository;

    public GridApplicationRepository getGridApplicationRepository() {
        return gridApplicationRepository;
    }

    @Autowired
    public void setGridApplicationRepository(GridApplicationRepository gridApplicationRepository) {
        this.gridApplicationRepository = gridApplicationRepository;
    }

    public List<GridApplication> listByStatus(String status) {
        return gridApplicationRepository.findByStatus(status);
    }

    public GridApplication submitApplication(GridApplication application) {
        application.setStatus("PENDING");
        application.setApplicationDate(new Date());
        return gridApplicationRepository.save(application);
    }

    public GridApplication auditApplication(Long id, String status, String opinion, String feedbackBy) {
        Optional<GridApplication> optional = gridApplicationRepository.findById(id);
        if (optional.isPresent()) {
            GridApplication application = optional.get();
            application.setStatus(status);
            application.setAcceptanceOpinion(opinion);
            application.setFeedbackBy(feedbackBy != null ? Long.parseLong(feedbackBy) : null);
            application.setFeedbackTime(new Date());
            return gridApplicationRepository.save(application);
        }
        return null;
    }

    public GridApplication setGridNo(Long id, String gridNo) {
        Optional<GridApplication> optional = gridApplicationRepository.findById(id);
        if (optional.isPresent()) {
            GridApplication application = optional.get();
            application.setGridNo(gridNo);
            return gridApplicationRepository.save(application);
        }
        return null;
    }

    public GridApplication getApplicationById(Long id) {
        return gridApplicationRepository.findById(id).orElse(null);
    }

    public GridApplication returnApplication(Long id, String returnReason) {
        Optional<GridApplication> optional = gridApplicationRepository.findById(id);
        if (optional.isPresent()) {
            GridApplication application = optional.get();
            application.setStatus("RETURNED");
            application.setReturnReason(returnReason);
            application.setReturnTime(new Date());
            return gridApplicationRepository.save(application);
        }
        return null;
    }

    public GridApplication resubmitApplication(Long id) {
        Optional<GridApplication> optional = gridApplicationRepository.findById(id);
        if (optional.isPresent()) {
            GridApplication application = optional.get();
            application.setStatus("PENDING");
            application.setResubmitTime(new Date());
            return gridApplicationRepository.save(application);
        }
        return null;
    }

    public List<GridApplication> listAll() {
        return gridApplicationRepository.findAll();
    }
}
