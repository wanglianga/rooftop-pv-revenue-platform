package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Inverter;
import com.pvplatform.repository.InverterRepository;
import com.pvplatform.service.InverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InverterServiceImpl implements InverterService {

    @Autowired
    private InverterRepository inverterRepository;

    @Override
    public Result<List<Inverter>> listByRoofAreaId(Long roofAreaId) {
        if (roofAreaId != null) {
            return Result.success(inverterRepository.findByRoofAreaId(roofAreaId));
        }
        return Result.success(inverterRepository.findAll());
    }

    @Override
    public Result<Inverter> getInverterById(Long id) {
        Optional<Inverter> inverter = inverterRepository.findById(id);
        return inverter.map(Result::success).orElseGet(() -> Result.error("逆变器不存在"));
    }

    @Override
    public Result<Inverter> saveInverter(Inverter inverter) {
        return Result.success(inverterRepository.save(inverter));
    }

    @Override
    public Result<Inverter> updateInverter(Inverter inverter) {
        if (inverter.getId() == null || !inverterRepository.existsById(inverter.getId())) {
            return Result.error("逆变器不存在");
        }
        return Result.success(inverterRepository.save(inverter));
    }

    @Override
    public Result<Void> deleteInverter(Long id) {
        if (!inverterRepository.existsById(id)) {
            return Result.error("逆变器不存在");
        }
        inverterRepository.deleteById(id);
        return Result.success();
    }
}
