package com.building_security_system.models;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectorReaction {
    private Detector detector;
    private String detectorAnswer;
    private LocalDateTime detectorReactionTime = LocalDateTime.now();

    @Override
    public String toString() {
        return String.format("[ %s ] - Спрацював %s датчик( id = %d), %s",
                detectorReactionTime.toString(), detector.getType().name(), detector.getId(), detectorAnswer);
    }
}
