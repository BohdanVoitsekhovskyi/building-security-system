package com.building_security_system.util;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<DetectorsCommand> commands;
    //private Logger logger; Need to be implemented



    //Формування списку на основі детекторів що є,потім на основі перестановок
    //TODO
    public void createCommands(List<Detector> detectors) {
        commands = new ArrayList<DetectorsCommand>();

        for (int i = 0; i < detectors.size(); i++) {
            List<Detector> singleDetectorList = new ArrayList<>();
            singleDetectorList.add(detectors.get(i));
            commands.add(new DetectorsCommand(singleDetectorList));

        }
        //add all permutations with 2 detectors
        for(int i = 0; i < detectors.size(); i++) {
            for(int j = i+1; j < detectors.size(); j++) {
                List<Detector> singleDetectorList = new ArrayList<>();
                singleDetectorList.add(detectors.get(i));
                singleDetectorList.add(detectors.get(j));
                commands.add(new DetectorsCommand(singleDetectorList));
            }
        }
    }

    /**
     *  Socket
     */
    //Викликає всі команди по черзі,і повертає їх реакції через сокет з затримкою в 500 мс
    public void invokeCommands() {

    }
}
