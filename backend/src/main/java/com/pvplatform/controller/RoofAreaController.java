package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RoofArea;
import com.pvplatform.service.RoofAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roof-areas")
public class RoofAreaController {

    @Autowired
    private RoofAreaService roofAreaService;

    @GetMapping
    public Result<List<RoofArea>> list() {
        return roofAreaService.listRoofAreas();
    }

    @GetMapping("/{id}")
    public Result<RoofArea> getById(@PathVariable Long id) {
        return roofAreaService.getRoofAreaById(id);
    }

    @PostMapping
    public Result<RoofArea> save(@RequestBody RoofArea roofArea) {
        return roofAreaService.saveRoofArea(roofArea);
    }

    @PutMapping
    public Result<RoofArea> update(@RequestBody RoofArea roofArea) {
        return roofAreaService.updateRoofArea(roofArea);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return roofAreaService.deleteRoofArea(id);
    }
}
