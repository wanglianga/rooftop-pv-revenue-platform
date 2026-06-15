package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.VoteRecord;
import com.pvplatform.service.VoteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote-records")
public class VoteRecordController {

    @Autowired
    private VoteRecordService voteRecordService;

    @GetMapping
    public Result<List<VoteRecord>> list(@RequestParam(required = false) Long topicId) {
        return Result.success(voteRecordService.listByTopicId(topicId));
    }

    @PostMapping
    public Result<VoteRecord> save(@RequestBody VoteRecord voteRecord) {
        VoteRecord result = voteRecordService.vote(
                voteRecord.getTopicId(),
                voteRecord.getOwnerId(),
                voteRecord.getVoteResult(),
                voteRecord.getRemark()
        );
        if (result == null) {
            return Result.error("您已对该主题投过票");
        }
        return Result.success(result);
    }

    @GetMapping("/result/{topicId}")
    public Result<Map<String, Object>> getResult(@PathVariable Long topicId) {
        return Result.success(voteRecordService.getVoteResult(topicId));
    }
}
