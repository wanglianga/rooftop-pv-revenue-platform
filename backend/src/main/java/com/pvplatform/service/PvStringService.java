package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.PvString;

import java.util.List;

public interface PvStringService {
    Result<List<PvString>> listByRoofAreaId(Long roofAreaId);
    Result<PvString> getPvStringById(Long id);
    Result<PvString> savePvString(PvString pvString);
    Result<PvString> updatePvString(PvString pvString);
    Result<Void> deletePvString(Long id);
}
