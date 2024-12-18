package com.building_security_system.strategy;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;

import java.util.ArrayList;
import java.util.List;

public class SortedSimulationStrategy implements SimulationStrategy {

    //Формування списку на основі детекторів що є,потім на основі перестановок
    @Override
    public List<DetectorsCommand> simulate(List<Detector> detectors) {

       List<DetectorsCommand> commands = new ArrayList<>();

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
        return commands;
    }
}
