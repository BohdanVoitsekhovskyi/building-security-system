package com.building_security_system.models;

import java.util.List;

public class DetectorsCommand {
    private List<Detector> detectors;

    public List<SystemReaction> invoke() {
        return null;
    }

    DetectorsCommand(List<Detector> detectors) {
        this.detectors = detectors;
    }
}
