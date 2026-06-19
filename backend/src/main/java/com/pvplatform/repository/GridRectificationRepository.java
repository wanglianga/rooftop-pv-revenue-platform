package com.pvplatform.repository;

import com.pvplatform.entity.GridRectification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridRectificationRepository extends JpaRepository<GridRectification, Long> {
    List<GridRectification> findByGridApplicationId(Long gridApplicationId);
    List<GridRectification> findByGridApplicationIdOrderBySubmitTimeDesc(Long gridApplicationId);
}
