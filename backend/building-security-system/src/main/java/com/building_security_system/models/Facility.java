package com.building_security_system.models;

import com.building_security_system.db_access.entities.FacilityEntity;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Builder
@ToString
@Data
public class Facility {
    private long id;
    private User user;
    List<Floor> floors;

    public static FacilityEntity toEntity(Facility facility) {
        return FacilityEntity.builder()
                .id(facility.id)
                .user(facility.user)
                .floors(facility.floors.stream().map(Floor::toEntity).toList())
                .build();
    }

    public static Facility toModel(FacilityEntity entity) {
        return Facility.builder()
                .id(entity.getId())
                .user(entity.getUser())
                .floors(entity.getFloors().stream().map(Floor::toModel).toList())
                .build();
    }

}
