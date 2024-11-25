package com.building_security_system.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemReaction {
    private List<DetectorReaction> detectorsReaction = new ArrayList<>();
}