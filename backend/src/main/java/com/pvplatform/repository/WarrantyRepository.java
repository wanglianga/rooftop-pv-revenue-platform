package com.pvplatform.repository;

import com.pvplatform.entity.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WarrantyRepository extends JpaRepository<Warranty, Long> {
    List<Warranty> findByDeviceTypeAndDeviceId(String deviceType, Long deviceId);

    @Query("SELECT w FROM Warranty w WHERE w.warrantyEnd BETWEEN :today AND :expireDate")
    List<Warranty> findExpiringWarranties(@Param("today") Date today, @Param("expireDate") Date expireDate);
}
