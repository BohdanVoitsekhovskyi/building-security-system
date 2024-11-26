package com.building_security_system.dto;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorReaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectorReactionDto {
    private Detector detector;
    private String detectorAnswer;
    private Calendar detectorReactionTime;

    public static DetectorReactionDto toDto(DetectorReaction detectorReaction) {
        Calendar calendar = Calendar.getInstance();
        return new DetectorReactionDto(detectorReaction.getDetector(), detectorReaction.getDetectorAnswer(), calendar);
    }
}
