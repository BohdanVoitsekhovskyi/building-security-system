package com.building_security_system.models;

import com.building_security_system.db_access.entities.FacilityLogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityLog {
    long id;
    List<SystemReaction> logMessages;
    boolean isFinished;


    public void log(SystemReaction message) {
        logMessages.add(message);
    }
    public static FacilityLogEntity toEntity(FacilityLog facilityLog){
        return FacilityLogEntity.builder()
                .id(facilityLog.getId())
                .logMessages(facilityLog.getLogMessages())
                .build();
    }
    public static FacilityLog toModel(FacilityLogEntity facilityLogEntity){
        return FacilityLog.builder()
                .id(facilityLogEntity.getId())
                .logMessages(facilityLogEntity.getLogMessages())
                .build();
    }
}