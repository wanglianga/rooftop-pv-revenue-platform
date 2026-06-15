package com.pvplatform.service;

import com.pvplatform.entity.OwnerNotice;
import com.pvplatform.repository.OwnerNoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OwnerNoticeService {

    private OwnerNoticeRepository ownerNoticeRepository;

    public OwnerNoticeRepository getOwnerNoticeRepository() {
        return ownerNoticeRepository;
    }

    @Autowired
    public void setOwnerNoticeRepository(OwnerNoticeRepository ownerNoticeRepository) {
        this.ownerNoticeRepository = ownerNoticeRepository;
    }

    public OwnerNotice publishNotice(OwnerNotice notice) {
        notice.setPublishTime(new Date());
        notice.setCreateTime(new Date());
        return ownerNoticeRepository.save(notice);
    }

    public List<OwnerNotice> listNotices() {
        return ownerNoticeRepository.findAll();
    }

    public List<OwnerNotice> listByTargetAudience(String audience) {
        return ownerNoticeRepository.findByTargetAudience(audience);
    }
}
