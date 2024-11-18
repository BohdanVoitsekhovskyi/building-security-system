package com.building_security_system.models.detectors;

import com.building_security_system.db_access.entities.DetectorEntity;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class Detector {
    private long id;
    private String description;

    public static DetectorEntity toEntity(Detector detector) {
        return DetectorEntity.builder()
                .id(detector.id)
                .description(detector.description)
                .build();
    }
    public static Detector toModel(DetectorEntity entity) {
        return Detector.builder()
                .description(entity.getDescription())
                .id(entity.getId())
                .build();
    }
}
