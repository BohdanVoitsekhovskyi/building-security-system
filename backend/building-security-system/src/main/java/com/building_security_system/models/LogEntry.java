package com.building_security_system.models;

import java.util.List;

public class LogEntry {
    private List<SystemReaction> systemReactions;
    private long facilityId;
    private User user;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(SystemReaction systemReaction : systemReactions){
            sb.append(systemReaction.toString())
                    .append("\n")
                    .append("для користувача: ")
                    .append(user)
                    .append("\n")
                    .append("для будинку: ")
                    .append(facilityId)
                    .append("\n\n");
        }
        return sb.toString();
    }
}
