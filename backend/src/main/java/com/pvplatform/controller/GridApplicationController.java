package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GridApplication;
import com.pvplatform.service.GridApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grid-applications")
public class GridApplicationController {

    @Autowired
    private GridApplicationService gridApplicationService;

    @GetMapping
    public Result<List<GridApplication>> list(@RequestParam(required = false) String status) {
        return Result.success(gridApplicationService.listByStatus(status));
    }

    @GetMapping("/{id}")
    public Result<GridApplication> getById(@PathVariable Long id) {
        return Result.success(gridApplicationService.getApplicationById(id));
    }

    @PostMapping
    public Result<GridApplication> save(@RequestBody GridApplication gridApplication) {
        return Result.success(gridApplicationService.submitApplication(gridApplication));
    }

    @PutMapping("/{id}/audit")
    public Result<GridApplication> audit(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String status = (String) params.get("status");
        String acceptanceOpinion = (String) params.get("acceptanceOpinion");
        String feedbackBy = (String) params.get("feedbackBy");
        return Result.success(gridApplicationService.auditApplication(id, status, acceptanceOpinion, feedbackBy));
    }

    @PutMapping("/{id}/grid-no")
    public Result<GridApplication> setGridNo(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String gridNo = params.get("gridNo");
        return Result.success(gridApplicationService.setGridNo(id, gridNo));
    }
}
