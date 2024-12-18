package com.building_security_system.models;

import com.building_security_system.db_access.entities.FacilityEntity;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@ToString
@Data
public class Facility {
    private long id;
    List<Floor> floors;

    public static FacilityEntity toEntity(Facility facility) {
        return FacilityEntity.builder()
                .id(facility.id)
                .floors(facility.floors.stream().map(Floor::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static Facility toModel(FacilityEntity entity) {
        return Facility.builder()
                .id(entity.getId())
                .floors(entity.getFloors().stream().map(Floor::toModel).collect(Collectors.toList()))
                .build();
    }
}