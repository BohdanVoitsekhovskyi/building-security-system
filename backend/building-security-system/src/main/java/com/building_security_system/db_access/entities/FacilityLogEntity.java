package com.building_security_system.db_access.entities;

import com.building_security_system.models.SystemReaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "logs")
public class FacilityLogEntity {
    @Id
    long id;
    List<SystemReaction> logMessages;
}
