package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.GridRectification;
import com.pvplatform.service.GridRectificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grid-rectifications")
public class GridRectificationController {

    @Autowired
    private GridRectificationService gridRectificationService;

    @GetMapping
    public Result<List<GridRectification>> list(@RequestParam Long gridApplicationId) {
        return Result.success(gridRectificationService.listByGridApplicationId(gridApplicationId));
    }

    @GetMapping("/{id}")
    public Result<GridRectification> getById(@PathVariable Long id) {
        GridRectification rectification = gridRectificationService.getById(id);
        if (rectification == null) {
            return Result.error("整改记录不存在");
        }
        return Result.success(rectification);
    }

    @PostMapping
    public Result<GridRectification> submit(@RequestBody GridRectification rectification) {
        return Result.success(gridRectificationService.submitRectification(rectification));
    }

    @PutMapping("/{id}/review")
    public Result<GridRectification> review(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String status = params.get("status");
        GridRectification result = gridRectificationService.reviewRectification(id, status);
        if (result == null) {
            return Result.error("整改记录不存在");
        }
        return Result.success(result);
    }
}
