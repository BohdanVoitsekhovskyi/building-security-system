package com.building_security_system.models;

import com.building_security_system.db_access.entities.FloorEntity;
import com.building_security_system.models.detectors.Detector;
import com.building_security_system.util.SvgToJsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Floor {
    private long id;
    private int floorNumber;
    private SvgToJsonParser.JsonContent placement;
    private List<Detector> detectors;

    public static FloorEntity toEntity(Floor floor) {
        return FloorEntity.builder()
                .id(floor.id)
                .floorNumber(floor.floorNumber)
                .placement(floor.placement)
                .detectorEntities(floor.detectors.stream().map(Detector::toEntity).toList())
                .build();
    }

    public static Floor toModel(FloorEntity entity) {
        return Floor.builder()
                .id(entity.getId())
                .floorNumber(entity.getFloorNumber())
                .placement(entity.getPlacement())
                .detectors(entity.getDetectorEntities().stream().map(Detector::toModel).toList())
                .build();
    }
}
