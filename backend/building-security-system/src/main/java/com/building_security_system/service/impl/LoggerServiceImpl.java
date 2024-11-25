package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.LoggerRepository;
import com.building_security_system.models.FacilityLog;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
