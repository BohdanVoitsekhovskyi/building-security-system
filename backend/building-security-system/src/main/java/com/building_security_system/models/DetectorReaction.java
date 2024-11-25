package com.building_security_system.models;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectorReaction {
    private Detector detector;
    private String detectorAnswer;
    private Calendar detectorReactionTime = Calendar.getInstance();
}
