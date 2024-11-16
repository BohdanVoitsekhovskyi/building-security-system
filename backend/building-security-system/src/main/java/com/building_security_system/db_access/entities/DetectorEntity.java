package com.building_security_system.db_access.entities;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document(collation = "detectors")
public class DetectorEntity {
    @Id
    private long id;
    private String description;
}
