package com.building_security_system.dto;

import com.building_security_system.models.DetectorReaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemReactionDto {
    private List<DetectorReactionDto> detectorsReaction = new ArrayList<>();
}
