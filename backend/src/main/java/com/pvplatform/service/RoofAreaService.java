package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.RoofArea;

import java.util.List;

public interface RoofAreaService {
    Result<List<RoofArea>> listRoofAreas();
    Result<RoofArea> getRoofAreaById(Long id);
    Result<RoofArea> saveRoofArea(RoofArea roofArea);
    Result<RoofArea> updateRoofArea(RoofArea roofArea);
    Result<Void> deleteRoofArea(Long id);
}
