package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Warranty;
import com.pvplatform.service.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warranties")
public class WarrantyController {

    @Autowired
    private WarrantyService warrantyService;

    @GetMapping
    public Result<List<Warranty>> list(
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) Long deviceId) {
        return warrantyService.listByDevice(deviceType, deviceId);
    }

    @PostMapping
    public Result<Warranty> save(@RequestBody Warranty warranty) {
        return warrantyService.saveWarranty(warranty);
    }

    @GetMapping("/expiring")
    public Result<List<Warranty>> getExpiring(@RequestParam(required = false, defaultValue = "30") Integer days) {
        return warrantyService.getExpiringWarranties(days);
    }
}
