package com.building_security_system.util;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;
import com.building_security_system.models.SystemReaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager {
    private List<DetectorsCommand> commands;
    private int currentCommandIndex;
    //private Logger logger; Need to be implemented



    //Формування списку на основі детекторів що є,потім на основі перестановок
    //TODO
    public void createCommands(List<Detector> detectors) {
        commands = new ArrayList<>();

        for (Detector detector : detectors) {
            List<Detector> singleDetectorList = new ArrayList<>();
            singleDetectorList.add(detector);
            commands.add(new DetectorsCommand(singleDetectorList));
        }
        //add all permutations with 2 detectors
        for (int i = 0; i < detectors.size(); i++) {
            for (int j = i + 1; j < detectors.size(); j++) {
                List<Detector> twoDetectorsList = new ArrayList<>();
                twoDetectorsList.add(detectors.get(i));
                twoDetectorsList.add(detectors.get(j));
                commands.add(new DetectorsCommand(twoDetectorsList));
            }
        }

        Collections.shuffle(commands);
    }

    /**
     *  Socket
     */
    //Викликає всі команди по черзі, і повертає їх реакції через сокет з затримкою в 500 мс
    public List<SystemReaction> invokeCommands() {
        return commands.get(currentCommandIndex++).invoke();
    }
}
