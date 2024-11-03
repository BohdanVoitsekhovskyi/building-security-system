package com.building_security_system.DataBaseAccess.Entities;


import com.building_security_system.Models.Position;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@Getter
@Document(collation = "detectors")
public class DetectorEntity {
    private ObjectId id;
    private Position position;
    private String description;
}
