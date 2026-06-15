package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.Inverter;

import java.util.List;

public interface InverterService {
    Result<List<Inverter>> listByRoofAreaId(Long roofAreaId);
    Result<Inverter> getInverterById(Long id);
    Result<Inverter> saveInverter(Inverter inverter);
    Result<Inverter> updateInverter(Inverter inverter);
    Result<Void> deleteInverter(Long id);
}
