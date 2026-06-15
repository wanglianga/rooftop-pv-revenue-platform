package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Inverter;
import com.pvplatform.service.InverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inverters")
public class InverterController {

    @Autowired
    private InverterService inverterService;

    @GetMapping
    public Result<List<Inverter>> list(@RequestParam(required = false) Long roofAreaId) {
        return inverterService.listByRoofAreaId(roofAreaId);
    }

    @GetMapping("/{id}")
    public Result<Inverter> getById(@PathVariable Long id) {
        return inverterService.getInverterById(id);
    }

    @PostMapping
    public Result<Inverter> save(@RequestBody Inverter inverter) {
        return inverterService.saveInverter(inverter);
    }

    @PutMapping
    public Result<Inverter> update(@RequestBody Inverter inverter) {
        return inverterService.updateInverter(inverter);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return inverterService.deleteInverter(id);
    }
}
