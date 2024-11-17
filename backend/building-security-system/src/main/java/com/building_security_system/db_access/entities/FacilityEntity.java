package com.building_security_system.db_access.entities;

import com.building_security_system.models.Floor;
import com.building_security_system.models.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "facilities")
public class FacilityEntity {
    @Id
    private long id;
    private User user;
    List<FloorEntity> floors;
}
