package com.building_security_system.models.detectors;

import com.building_security_system.db_access.entities.DetectorEntity;
import com.building_security_system.dto.DetectorDto;
import com.building_security_system.models.Position;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class Detector {
    private long id;
    private DetectorType type;
    private Position position;

    public static DetectorEntity toEntity(Detector detector) {
        return DetectorEntity.builder()
                .id(detector.id)
                .type(detector.type.toString())
                .position(detector.position)
                .build();
    }
    public static Detector toModel(DetectorEntity entity) {
        return Detector.builder()
                .id(entity.getId())
                .type(DetectorType.valueOf(entity.getType().toUpperCase()))
                .position(entity.getPosition())
                .build();
    }

    public static DetectorDto modelToDto(Detector detector) {
        return DetectorDto.builder()
                .id(detector.getId())
                .type(detector.type.toString())
                .position(detector.position)
                .build();
    }

    public static Detector dtoToModel(DetectorDto dto) {
        return Detector.builder()
                .id(dto.getId())
                .type(DetectorType.valueOf(dto.getType().toUpperCase()))
                .position(dto.getPosition())
                .build();
    }

    public enum DetectorType {
        SMOKE("smoke"), FLOOD("flood"),
        TEMPERATURE("temperature"), MOTION("motion");

        final String detectorTypeName;

        DetectorType(String detectorTypeName) {
            this.detectorTypeName = detectorTypeName;
        }

        @Override
        public String toString() {
            return detectorTypeName;
        }
    }
}