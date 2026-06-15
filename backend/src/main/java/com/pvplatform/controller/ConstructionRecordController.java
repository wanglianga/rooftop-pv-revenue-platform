package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.ConstructionRecord;
import com.pvplatform.service.ConstructionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/construction-records")
public class ConstructionRecordController {

    @Autowired
    private ConstructionRecordService constructionRecordService;

    @GetMapping
    public Result<List<ConstructionRecord>> list(
            @RequestParam(required = false) Long roofAreaId,
            @RequestParam(required = false) String stage) {
        if (roofAreaId != null) {
            return Result.success(constructionRecordService.listByRoofAreaId(roofAreaId));
        } else if (stage != null) {
            return Result.success(constructionRecordService.listByStage(stage));
        }
        return Result.error("请提供 roofAreaId 或 stage 参数");
    }

    @PostMapping
    public Result<ConstructionRecord> save(@RequestBody ConstructionRecord constructionRecord) {
        return Result.success(constructionRecordService.saveRecord(constructionRecord));
    }
}
