package com.pvplatform.service;

import com.pvplatform.entity.VoteTopic;
import com.pvplatform.repository.VoteTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoteTopicService {

    private VoteTopicRepository voteTopicRepository;

    public VoteTopicRepository getVoteTopicRepository() {
        return voteTopicRepository;
    }

    @Autowired
    public void setVoteTopicRepository(VoteTopicRepository voteTopicRepository) {
        this.voteTopicRepository = voteTopicRepository;
    }

    public VoteTopic createTopic(VoteTopic topic) {
        topic.setStatus(0);
        topic.setCreateTime(new Date());
        return voteTopicRepository.save(topic);
    }

    public List<VoteTopic> listTopics() {
        return voteTopicRepository.findAll();
    }

    public VoteTopic publishTopic(Long id) {
        Optional<VoteTopic> optional = voteTopicRepository.findById(id);
        if (optional.isPresent()) {
            VoteTopic topic = optional.get();
            topic.setStatus(1);
            topic.setStartDate(new Date());
            return voteTopicRepository.save(topic);
        }
        return null;
    }

    public VoteTopic closeTopic(Long id) {
        Optional<VoteTopic> optional = voteTopicRepository.findById(id);
        if (optional.isPresent()) {
            VoteTopic topic = optional.get();
            topic.setStatus(2);
            topic.setEndDate(new Date());
            return voteTopicRepository.save(topic);
        }
        return null;
    }
}
