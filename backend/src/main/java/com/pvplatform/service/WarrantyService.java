package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Warranty;

import java.util.List;

public interface WarrantyService {
    Result<List<Warranty>> listByDevice(String deviceType, Long deviceId);
    Result<Warranty> saveWarranty(Warranty warranty);
    Result<List<Warranty>> getExpiringWarranties(Integer days);
}
