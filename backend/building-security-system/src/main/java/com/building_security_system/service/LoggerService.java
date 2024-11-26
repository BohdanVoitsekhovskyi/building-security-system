package com.building_security_system.service;

import com.building_security_system.models.FacilityLog;
import com.building_security_system.models.SystemReaction;

import java.io.File;

public interface LoggerService {

    void log(SystemReaction message,long logId);
    FacilityLog getLog(long logId);
    void saveLog(FacilityLog log);
    byte[] getLogAsByteArray( long logId);

}
