package com.building_security_system.models;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectorReaction {
    private Detector detector;
    private String detectorAnswer;
    private Calendar detectorReactionTime = Calendar.getInstance();
}
