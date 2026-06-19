package com.pvplatform.service;

import com.pvplatform.entity.InverterAnomaly;
import com.pvplatform.repository.InverterAnomalyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InverterAnomalyService {

    @Autowired
    private InverterAnomalyRepository inverterAnomalyRepository;

    public List<InverterAnomaly> listAll() {
        return inverterAnomalyRepository.findAll();
    }

    public List<InverterAnomaly> listByInverterId(Long inverterId) {
        return inverterAnomalyRepository.findByInverterIdOrderByDiscoveryTimeDesc(inverterId);
    }

    public List<InverterAnomaly> listByStatus(String status) {
        return inverterAnomalyRepository.findByStatus(status);
    }

    public InverterAnomaly getById(Long id) {
        return inverterAnomalyRepository.findById(id).orElse(null);
    }

    public InverterAnomaly createAnomaly(InverterAnomaly anomaly) {
        anomaly.setCreateTime(new Date());
        if (anomaly.getStatus() == null) {
            anomaly.setStatus("OPEN");
        }
        if (anomaly.getDiscoveryTime() == null) {
            anomaly.setDiscoveryTime(new Date());
        }
        return inverterAnomalyRepository.save(anomaly);
    }

    public InverterAnomaly updateAnomaly(InverterAnomaly anomaly) {
        return inverterAnomalyRepository.findById(anomaly.getId()).map(existing -> {
            if (anomaly.getAnomalyType() != null) existing.setAnomalyType(anomaly.getAnomalyType());
            if (anomaly.getDescription() != null) existing.setDescription(anomaly.getDescription());
            if (anomaly.getAffectedPvStrings() != null) existing.setAffectedPvStrings(anomaly.getAffectedPvStrings());
            if (anomaly.getAffectedBuilding() != null) existing.setAffectedBuilding(anomaly.getAffectedBuilding());
            if (anomaly.getDowntimeHours() != null) existing.setDowntimeHours(anomaly.getDowntimeHours());
            if (anomaly.getTroubleshootProcess() != null) existing.setTroubleshootProcess(anomaly.getTroubleshootProcess());
            if (anomaly.getAffectedDates() != null) existing.setAffectedDates(anomaly.getAffectedDates());
            if (anomaly.getOperator() != null) existing.setOperator(anomaly.getOperator());
            if (anomaly.getStatus() != null) existing.setStatus(anomaly.getStatus());
            return inverterAnomalyRepository.save(existing);
        }).orElse(null);
    }

    public InverterAnomaly completeRepair(Long id, String repairResult, String beforeCurveDate, String afterCurveDate, String curveComparisonResult) {
        return inverterAnomalyRepository.findById(id).map(anomaly -> {
            anomaly.setRepairTime(new Date());
            anomaly.setRepairResult(repairResult);
            anomaly.setBeforeCurveDate(beforeCurveDate);
            anomaly.setAfterCurveDate(afterCurveDate);
            anomaly.setCurveComparisonResult(curveComparisonResult);
            anomaly.setStatus("RESOLVED");
            return inverterAnomalyRepository.save(anomaly);
        }).orElse(null);
    }

    public InverterAnomaly closeAnomaly(Long id) {
        return inverterAnomalyRepository.findById(id).map(anomaly -> {
            anomaly.setStatus("CLOSED");
            return inverterAnomalyRepository.save(anomaly);
        }).orElse(null);
    }
}
