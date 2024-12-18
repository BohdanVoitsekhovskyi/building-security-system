package com.building_security_system.strategy;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;

import java.util.List;

public interface SimulationStrategy {
    List<DetectorsCommand> simulate(List<Detector> detectors);
}
