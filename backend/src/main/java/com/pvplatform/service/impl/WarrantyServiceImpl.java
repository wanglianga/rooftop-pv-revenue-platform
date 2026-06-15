package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Warranty;
import com.pvplatform.repository.WarrantyRepository;
import com.pvplatform.service.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WarrantyServiceImpl implements WarrantyService {

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Override
    public Result<List<Warranty>> listByDevice(String deviceType, Long deviceId) {
        if (deviceType != null && deviceId != null) {
            return Result.success(warrantyRepository.findByDeviceTypeAndDeviceId(deviceType, deviceId));
        }
        return Result.success(warrantyRepository.findAll());
    }

    @Override
    public Result<Warranty> saveWarranty(Warranty warranty) {
        return Result.success(warrantyRepository.save(warranty));
    }

    @Override
    public Result<List<Warranty>> getExpiringWarranties(Integer days) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, days != null ? days : 30);
        Date expireDate = calendar.getTime();
        return Result.success(warrantyRepository.findExpiringWarranties(today, expireDate));
    }
}
