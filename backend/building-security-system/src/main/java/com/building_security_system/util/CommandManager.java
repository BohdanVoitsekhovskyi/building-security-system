package com.building_security_system.util;

import com.building_security_system.models.Detector;
import com.building_security_system.models.DetectorsCommand;

import java.util.List;

public class CommandManager {
    private List<DetectorsCommand> commands;
    //private Logger logger; Need to be implemented


    //Формування списку на основі детекторів що є,потім на основі перестановок
    //
    public void createCommands(List<Detector> detectors) {

    }

    /**
     *  Socket
     */
    //Викликає всі команди по черзі,і повертає їх реакції через сокет з затримкою в 500 мс
    public void invokeCommands() {

    }
}
