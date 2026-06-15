package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RoofArea;
import com.pvplatform.repository.RoofAreaRepository;
import com.pvplatform.service.RoofAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoofAreaServiceImpl implements RoofAreaService {

    @Autowired
    private RoofAreaRepository roofAreaRepository;

    @Override
    public Result<List<RoofArea>> listRoofAreas() {
        return Result.success(roofAreaRepository.findAll());
    }

    @Override
    public Result<RoofArea> getRoofAreaById(Long id) {
        Optional<RoofArea> roofArea = roofAreaRepository.findById(id);
        return roofArea.map(Result::success).orElseGet(() -> Result.error("屋顶区域不存在"));
    }

    @Override
    public Result<RoofArea> saveRoofArea(RoofArea roofArea) {
        return Result.success(roofAreaRepository.save(roofArea));
    }

    @Override
    public Result<RoofArea> updateRoofArea(RoofArea roofArea) {
        if (roofArea.getId() == null || !roofAreaRepository.existsById(roofArea.getId())) {
            return Result.error("屋顶区域不存在");
        }
        return Result.success(roofAreaRepository.save(roofArea));
    }

    @Override
    public Result<Void> deleteRoofArea(Long id) {
        if (!roofAreaRepository.existsById(id)) {
            return Result.error("屋顶区域不存在");
        }
        roofAreaRepository.deleteById(id);
        return Result.success();
    }
}
