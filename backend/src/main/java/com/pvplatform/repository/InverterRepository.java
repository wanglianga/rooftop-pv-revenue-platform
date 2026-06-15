package com.pvplatform.repository;

import com.pvplatform.entity.Inverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InverterRepository extends JpaRepository<Inverter, Long> {
    List<Inverter> findByRoofAreaId(Long roofAreaId);
}
