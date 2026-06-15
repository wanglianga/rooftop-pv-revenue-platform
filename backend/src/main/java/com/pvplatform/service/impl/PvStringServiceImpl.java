package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PvString;
import com.pvplatform.repository.PvStringRepository;
import com.pvplatform.service.PvStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PvStringServiceImpl implements PvStringService {

    @Autowired
    private PvStringRepository pvStringRepository;

    @Override
    public Result<List<PvString>> listByRoofAreaId(Long roofAreaId) {
        if (roofAreaId != null) {
            return Result.success(pvStringRepository.findByRoofAreaId(roofAreaId));
        }
        return Result.success(pvStringRepository.findAll());
    }

    @Override
    public Result<PvString> getPvStringById(Long id) {
        Optional<PvString> pvString = pvStringRepository.findById(id);
        return pvString.map(Result::success).orElseGet(() -> Result.error("光伏组串不存在"));
    }

    @Override
    public Result<PvString> savePvString(PvString pvString) {
        return Result.success(pvStringRepository.save(pvString));
    }

    @Override
    public Result<PvString> updatePvString(PvString pvString) {
        if (pvString.getId() == null || !pvStringRepository.existsById(pvString.getId())) {
            return Result.error("光伏组串不存在");
        }
        return Result.success(pvStringRepository.save(pvString));
    }

    @Override
    public Result<Void> deletePvString(Long id) {
        if (!pvStringRepository.existsById(id)) {
            return Result.error("光伏组串不存在");
        }
        pvStringRepository.deleteById(id);
        return Result.success();
    }
}
