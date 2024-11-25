package com.building_security_system.models;

import java.util.List;

public class DetectorsCommand {
    private final List<Detector> detectors;
    public DetectorsCommand(List<Detector> detectors) {
        this.detectors = detectors;
    }

    public SystemReaction invoke() {
        SystemReaction reaction = new SystemReaction();

        for (Detector detector : detectors) {
            DetectorReaction detectorReaction = detector.invoke();
            reaction.getDetectorsReaction().add(detectorReaction);
        }

        return reaction;
    }
}
