package com.building_security_system.util;

import java.util.ArrayList;
import java.util.List;

public class BuildingSecuritySystemLogger {
    private static BuildingSecuritySystemLogger instance;
    private static List<String> messages = new ArrayList<>();

    public static BuildingSecuritySystemLogger getInstance() {
        if (instance == null) {
            instance = new BuildingSecuritySystemLogger();
        }
        return instance;
    }

    public void log(LogEntry message){
        messages.add(message.toString());
    }

    public List<String> getLog() {
        List<String> log = List.copyOf(messages);
        messages.clear();
        return log;
    }
}
