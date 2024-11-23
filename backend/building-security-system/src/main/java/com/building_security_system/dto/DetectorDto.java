package com.building_security_system.dto;

import com.building_security_system.models.Position;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DetectorDto {
    private long id;
    private long furnitureId;
    private String type;
    private Position position;
}