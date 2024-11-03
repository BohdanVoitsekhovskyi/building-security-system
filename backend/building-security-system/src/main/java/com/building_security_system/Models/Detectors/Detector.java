package com.building_security_system.Models.Detectors;

import com.building_security_system.DataBaseAccess.Entities.DetectorEntity;
import com.building_security_system.Models.Position;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public abstract class Detector {
    private ObjectId id;
    private Position position;
    private String description;

    public static DetectorEntity toEntity(Detector detector) {
        return DetectorEntity.builder()
                .id(detector.id)
                .description(detector.description)
                .position(detector.position)
                .build();
    }
    public static Detector toModel(DetectorEntity entity) {
        return Detector.builder()
                .description(entity.getDescription())
                .id(entity.getId())
                .position(entity.getPosition())
                .build();
    }
}
