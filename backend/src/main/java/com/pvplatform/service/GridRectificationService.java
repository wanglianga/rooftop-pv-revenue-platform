package com.pvplatform.service;

import com.pvplatform.entity.GridRectification;
import com.pvplatform.repository.GridRectificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GridRectificationService {

    @Autowired
    private GridRectificationRepository gridRectificationRepository;

    public List<GridRectification> listByGridApplicationId(Long gridApplicationId) {
        return gridRectificationRepository.findByGridApplicationIdOrderBySubmitTimeDesc(gridApplicationId);
    }

    public GridRectification submitRectification(GridRectification rectification) {
        rectification.setSubmitTime(new Date());
        rectification.setStatus("SUBMITTED");
        rectification.setCreateTime(new Date());
        return gridRectificationRepository.save(rectification);
    }

    public GridRectification reviewRectification(Long id, String status) {
        return gridRectificationRepository.findById(id).map(r -> {
            r.setStatus(status);
            return gridRectificationRepository.save(r);
        }).orElse(null);
    }

    public GridRectification getById(Long id) {
        return gridRectificationRepository.findById(id).orElse(null);
    }
}
