package com.building_security_system.db_access.entities;

import com.building_security_system.util.SvgToJsonParser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "floors")
public class FloorEntity {
    @Id
    private long id;
    private SvgToJsonParser.JsonContent placement;
    private List<DetectorEntity> detectorEntities;
}