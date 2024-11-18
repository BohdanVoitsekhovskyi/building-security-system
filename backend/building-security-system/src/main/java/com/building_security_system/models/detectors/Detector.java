package com.building_security_system.models.detectors;

import com.building_security_system.db_access.entities.DetectorEntity;
import com.building_security_system.models.Position;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class Detector {
    private long id;
    private String description;
    private Position position;

    public static DetectorEntity toEntity(Detector detector) {
        return DetectorEntity.builder()
                .id(detector.id)
                .description(detector.description)
                .position(detector.position)
                .build();
    }
    public static Detector toModel(DetectorEntity entity) {
        return Detector.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .position(entity.getPosition())
                .build();
    }
}
