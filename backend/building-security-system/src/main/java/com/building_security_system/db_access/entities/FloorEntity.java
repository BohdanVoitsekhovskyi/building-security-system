package com.building_security_system.db_access.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "floors")
public class FloorEntity {
}