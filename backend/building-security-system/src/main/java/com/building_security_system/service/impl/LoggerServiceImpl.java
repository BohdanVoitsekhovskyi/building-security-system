package com.building_security_system.service.impl;

import com.building_security_system.db_access.entities.FacilityLogEntity;
import com.building_security_system.db_access.repositories.LoggerRepository;
import com.building_security_system.models.FacilityLog;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoggerServiceImpl implements LoggerService {

    private final LoggerRepository loggerRepository;

    @Autowired
    public LoggerServiceImpl(LoggerRepository loggerRepository) {
        this.loggerRepository = loggerRepository;
    }
    @Override
    public void log(SystemReaction message,long logId) {
        FacilityLog facilityLogger = FacilityLog.toModel(loggerRepository.findOneById(logId));
        facilityLogger.log(message);

        loggerRepository.save(FacilityLog.toEntity(facilityLogger));
    }
    @Override
    public FacilityLog getLog(long logId) {
        return FacilityLog.toModel(loggerRepository.findOneById(logId));
    }

    @Override
    public void saveLog(FacilityLog log) {
        loggerRepository.save(FacilityLog.toEntity(log));
    }

    @Override
    public byte[] getLogAsByteArray(long logId) {
        FacilityLog facilityLogger = FacilityLog.toModel(loggerRepository.findOneById(logId));

        StringBuilder stringBuilder = new StringBuilder();

        for(SystemReaction sr:facilityLogger.getLogMessages()){
            stringBuilder.append(sr);
        }

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }




    @Override
    public void eraseLogs(long facilityId) {
        FacilityLogEntity logEntity = loggerRepository.findOneById(facilityId);
        logEntity.setLogMessages(new ArrayList<>());
        loggerRepository.save(logEntity);
    }
}
