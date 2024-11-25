package com.building_security_system.controllers;

import com.building_security_system.models.Facility;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.FacilityService;
import com.building_security_system.util.CommandManager;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Controller
@Log4j2
@CrossOrigin
public class SocketController {
    private FacilityService facilityService;
    private final ExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final CommandManager commandManager = new CommandManager();

    // Maps to track facility states
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityStopFlags = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityPauseFlags = new ConcurrentHashMap<>();

    @Autowired
    public SocketController(SocketIOServer socketServer, FacilityService facilityService) {
        this.facilityService = facilityService;

        socketServer.addConnectListener(onUserConnectWithSocket);
        socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        socketServer.addEventListener("testing-system", Long.class, onSendMessage);
        socketServer.addEventListener("stop-resume-testing", String.class, onStopResume);
    }

    public ConnectListener onUserConnectWithSocket = client ->
            log.info("Client connected: {}", client.getSessionId());

    public DisconnectListener onUserDisconnectWithSocket = client ->
            log.info("Client disconnected: {}", client.getSessionId());

    public DataListener<String> onStopResume = (client, command, ackRequest) -> {
        String[] parts = command.split(":");
        if (parts.length != 2) {
            client.sendEvent("error", "Invalid command format. Expected format: STOP/RESUME:facilityId");
            return;
        }

        String action = parts[0];
        long facilityId = Long.parseLong(parts[1]);

        switch (action.toUpperCase()) {
            case "STOP":
                facilityPauseFlags.computeIfAbsent(facilityId, id -> new AtomicBoolean(false)).set(true);
                log.info("Stop signal received for facility: {}", facilityId);
                break;
            case "RESUME":
                facilityStopFlags.computeIfAbsent(facilityId, id -> new AtomicBoolean(false)).set(false);
                facilityPauseFlags.computeIfAbsent(facilityId, id -> new AtomicBoolean(true)).set(false);
                log.info("Resume signal received for facility: {}", facilityId);
                break;
            default:
                client.sendEvent("error", "Invalid action. Expected STOP or RESUME.");
        }
    };

    public DataListener<Long> onSendMessage = (client, facilityId, ackRequest) -> {
        log.info("Requested floors list of facility with id: {}", facilityId);
        Facility facility = facilityService.getFacilityById(facilityId);

        if (facility == null) {
            client.sendEvent("error", "Invalid facility ID: " + facilityId);
            return;
        }

        AtomicBoolean stopFlag = facilityStopFlags.computeIfAbsent(facilityId, id -> new AtomicBoolean(false));
        AtomicBoolean pauseFlag = facilityPauseFlags.computeIfAbsent(facilityId, id -> new AtomicBoolean(false));

        executorService.submit(() -> {
            commandManager.createCommands(facility.getFloors()
                    .stream()
                    .flatMap(floor -> floor.getDetectors().stream())
                    .collect(Collectors.toList()));

            try {
                while (!stopFlag.get()) {
                    if (pauseFlag.get()) {
                        Thread.sleep(500); // Wait briefly while paused
                        continue;
                    }

                    List<SystemReaction> systemReactions = commandManager.invokeCommands();
                    if (systemReactions == null) {
                        break;
                    }
                    client.sendEvent("floorsList", systemReactions);

                    Thread.sleep(1500); // Adjust delay as needed
                }
                log.info("Processing terminated for facility: {}", facilityId);
            } catch (InterruptedException e) {
                log.error("Processing interrupted for facility: {}", facilityId, e);
                Thread.currentThread().interrupt();
            }
        });
    };
}
