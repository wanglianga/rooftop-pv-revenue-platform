package com.pvplatform.service;

import com.pvplatform.entity.VoteRecord;
import com.pvplatform.repository.VoteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteRecordService {

    private VoteRecordRepository voteRecordRepository;

    public VoteRecordRepository getVoteRecordRepository() {
        return voteRecordRepository;
    }

    @Autowired
    public void setVoteRecordRepository(VoteRecordRepository voteRecordRepository) {
        this.voteRecordRepository = voteRecordRepository;
    }

    public VoteRecord vote(Long topicId, Long ownerId, String result, String remark) {
        if (voteRecordRepository.existsByTopicIdAndOwnerId(topicId, ownerId)) {
            return null;
        }
        VoteRecord record = new VoteRecord();
        record.setTopicId(topicId);
        record.setOwnerId(ownerId);
        record.setVoteResult(result);
        record.setRemark(remark);
        record.setVoteTime(new Date());
        return voteRecordRepository.save(record);
    }

    public List<VoteRecord> listByTopicId(Long topicId) {
        return voteRecordRepository.findByTopicId(topicId);
    }

    public Map<String, Object> getVoteResult(Long topicId) {
        List<VoteRecord> records = voteRecordRepository.findByTopicId(topicId);
        Map<String, Object> result = new HashMap<>();
        int agreeCount = 0;
        int disagreeCount = 0;
        int abstainCount = 0;
        for (VoteRecord record : records) {
            String voteResult = record.getVoteResult();
            if ("AGREE".equals(voteResult)) {
                agreeCount++;
            } else if ("DISAGREE".equals(voteResult)) {
                disagreeCount++;
            } else if ("ABSTAIN".equals(voteResult)) {
                abstainCount++;
            }
        }
        result.put("topicId", topicId);
        result.put("totalVotes", records.size());
        result.put("agreeCount", agreeCount);
        result.put("disagreeCount", disagreeCount);
        result.put("abstainCount", abstainCount);
        return result;
    }
}
