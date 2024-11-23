package com.building_security_system.models;

import java.util.ArrayList;
import java.util.List;

public class DetectorsCommand {
    private final List<Detector> detectors;
    DetectorsCommand(List<Detector> detectors) {
        this.detectors = detectors;
    }

    public List<SystemReaction> invoke() {
        return detectors.stream().map(Detector::invoke).toList();
    }
}