package com.pvplatform.repository;

import com.pvplatform.entity.PvString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PvStringRepository extends JpaRepository<PvString, Long> {
    List<PvString> findByRoofAreaId(Long roofAreaId);
}
