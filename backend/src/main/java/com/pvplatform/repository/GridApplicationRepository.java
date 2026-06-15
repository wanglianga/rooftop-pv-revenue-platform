package com.pvplatform.repository;

import com.pvplatform.entity.GridApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridApplicationRepository extends JpaRepository<GridApplication, Long> {
    List<GridApplication> findByStatus(String status);
}
