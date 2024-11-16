package com.building_security_system.models;

import com.building_security_system.db_access.entities.FacilityEntity;
import lombok.Builder;
import org.bson.types.ObjectId;
import java.util.List;

@Builder
public class Facility {
    private long id;
    private User user;
    List<Floor> floors;

    public static FacilityEntity toEntity(Facility facility) {
        return FacilityEntity.builder()
                .id(facility.id)
                .user(facility.user)
                .floors(facility.floors)
                .build();
    }

    public static Facility toModel(FacilityEntity entity) {
        return Facility.builder()
                .id(entity.getId())
                .user(entity.getUser())
                .floors(entity.getFloors())
                .build();
    }

}
