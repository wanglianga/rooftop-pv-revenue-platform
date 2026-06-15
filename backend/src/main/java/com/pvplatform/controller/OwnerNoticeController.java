package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.OwnerNotice;
import com.pvplatform.service.OwnerNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner-notices")
public class OwnerNoticeController {

    @Autowired
    private OwnerNoticeService ownerNoticeService;

    @GetMapping
    public Result<List<OwnerNotice>> list(@RequestParam(required = false) String audience) {
        if (audience != null) {
            return Result.success(ownerNoticeService.listByTargetAudience(audience));
        }
        return Result.success(ownerNoticeService.listNotices());
    }

    @PostMapping
    public Result<OwnerNotice> save(@RequestBody OwnerNotice ownerNotice) {
        return Result.success(ownerNoticeService.publishNotice(ownerNotice));
    }
}
