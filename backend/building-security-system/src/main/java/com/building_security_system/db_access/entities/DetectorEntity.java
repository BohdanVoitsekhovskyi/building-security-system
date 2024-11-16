package com.building_security_system.db_access.entities;

import com.building_security_system.models.Position;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document(collation = "detectors")
public class DetectorEntity {
    private long id;
    private Position position;
    private String description;
}
