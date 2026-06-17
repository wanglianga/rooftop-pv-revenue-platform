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
        if (status != null && !status.isEmpty()) {
            return Result.success(gridApplicationService.listByStatus(status));
        }
        return Result.success(gridApplicationService.listAll());
    }

    @GetMapping("/{id}")
    public Result<GridApplication> getById(@PathVariable Long id) {
        GridApplication app = gridApplicationService.getApplicationById(id);
        if (app == null) {
            return Result.error("并网申请不存在");
        }
        return Result.success(app);
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

    @PutMapping("/{id}/return")
    public Result<GridApplication> returnApplication(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String returnReason = params.get("returnReason");
        GridApplication result = gridApplicationService.returnApplication(id, returnReason);
        if (result == null) {
            return Result.error("并网申请不存在");
        }
        return Result.success(result);
    }

    @PutMapping("/{id}/resubmit")
    public Result<GridApplication> resubmitApplication(@PathVariable Long id) {
        GridApplication result = gridApplicationService.resubmitApplication(id);
        if (result == null) {
            return Result.error("并网申请不存在");
        }
        return Result.success(result);
    }
}
