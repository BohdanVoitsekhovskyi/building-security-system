package com.building_security_system.models;

import java.util.List;
import java.util.logging.Logger;

public class DetectorsCommand {
    private final List<Detector> detectors;
    public DetectorsCommand(List<Detector> detectors) {
        this.detectors = detectors;
    }

    public List<SystemReaction> invoke() {
//        Logger logger = Logger.getLogger("alk");
//        logger.

        return detectors.stream().map(Detector::invoke).toList();
    }
}
