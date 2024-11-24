package com.building_security_system.db_access.entities;

import com.building_security_system.models.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "detectors")
public class DetectorEntity {
    @Id
    private long id;
    private long furnitureId;
    private String type;
    private Position position;
}