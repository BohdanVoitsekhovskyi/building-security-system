package com.building_security_system.strategy;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;

import java.util.Collections;
import java.util.List;

public class RandomSimulationStrategy implements SimulationStrategy {

    @Override
    public List<DetectorsCommand> simulate(List<Detector> detectors) {

        SortedSimulationStrategy strategy = new SortedSimulationStrategy();

        List<DetectorsCommand> commands = strategy.simulate(detectors);

        Collections.shuffle(commands);

        return commands;
    }
}
