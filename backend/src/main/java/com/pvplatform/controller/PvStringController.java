package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PvString;
import com.pvplatform.service.PvStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pv-strings")
public class PvStringController {

    @Autowired
    private PvStringService pvStringService;

    @GetMapping
    public Result<List<PvString>> list(@RequestParam(required = false) Long roofAreaId) {
        return pvStringService.listByRoofAreaId(roofAreaId);
    }

    @GetMapping("/{id}")
    public Result<PvString> getById(@PathVariable Long id) {
        return pvStringService.getPvStringById(id);
    }

    @PostMapping
    public Result<PvString> save(@RequestBody PvString pvString) {
        return pvStringService.savePvString(pvString);
    }

    @PutMapping
    public Result<PvString> update(@RequestBody PvString pvString) {
        return pvStringService.updatePvString(pvString);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return pvStringService.deletePvString(id);
    }
}
