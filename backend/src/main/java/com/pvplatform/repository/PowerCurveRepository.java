package com.pvplatform.repository;

import com.pvplatform.entity.PowerCurve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PowerCurveRepository extends JpaRepository<PowerCurve, Long> {
    @Query("SELECT p FROM PowerCurve p WHERE p.inverterId = :inverterId AND DATE(p.recordTime) = DATE(:date)")
    List<PowerCurve> findByInverterIdAndDate(@Param("inverterId") Long inverterId, @Param("date") Date date);
}
