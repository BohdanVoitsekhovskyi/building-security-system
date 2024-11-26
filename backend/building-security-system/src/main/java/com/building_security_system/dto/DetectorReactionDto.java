package com.building_security_system.dto;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorReaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectorReactionDto {
    private Detector detector;
    private String detectorAnswer;
    private Calendar detectorReactionTime;

    public static DetectorReactionDto toDto(DetectorReaction detectorReaction) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(detectorReaction.getDetectorReactionTime().atZone(ZoneId.systemDefault()).toEpochSecond()));
        return new DetectorReactionDto(detectorReaction.getDetector(), detectorReaction.getDetectorAnswer(), calendar);
    }
}
