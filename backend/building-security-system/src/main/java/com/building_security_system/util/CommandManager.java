package com.building_security_system.util;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.strategy.SimulationStrategy;

import java.util.List;

public class CommandManager {
    private List<DetectorsCommand> commands;
    private int currentCommandIndex;
    private final SimulationStrategy simulationStrategy;


    public CommandManager(SimulationStrategy simulationStrategy) {
        this.simulationStrategy = simulationStrategy;
    }

    public void createCommands(List<Detector> detectors) {
       commands = simulationStrategy.simulate(detectors);
    }

    //Викликає всі команди по черзі, і повертає їх реакції через сокет з затримкою в 500 мс
    public SystemReaction invokeCommands() {
        if (commands.size() <= currentCommandIndex) {
            return null;
        }
        return commands.get(currentCommandIndex++).invoke();
    }
}