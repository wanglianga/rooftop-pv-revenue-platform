package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.VoteTopic;
import com.pvplatform.service.VoteTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vote-topics")
public class VoteTopicController {

    @Autowired
    private VoteTopicService voteTopicService;

    @GetMapping
    public Result<List<VoteTopic>> list() {
        return Result.success(voteTopicService.listTopics());
    }

    @PostMapping
    public Result<VoteTopic> save(@RequestBody VoteTopic voteTopic) {
        return Result.success(voteTopicService.createTopic(voteTopic));
    }

    @PutMapping("/{id}/publish")
    public Result<VoteTopic> publish(@PathVariable Long id) {
        return Result.success(voteTopicService.publishTopic(id));
    }

    @PutMapping("/{id}/close")
    public Result<VoteTopic> close(@PathVariable Long id) {
        return Result.success(voteTopicService.closeTopic(id));
    }
}
