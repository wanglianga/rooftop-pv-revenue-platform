package com.pvplatform.repository;

import com.pvplatform.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    List<MeterReading> findByInverterId(Long inverterId);

    @Query("SELECT m FROM MeterReading m WHERE YEAR(m.readingDate) = :year AND MONTH(m.readingDate) = :month")
    List<MeterReading> findByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);
}
