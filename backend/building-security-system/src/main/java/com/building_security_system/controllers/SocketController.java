package com.building_security_system.controllers;

import com.building_security_system.dto.SocketCommandDto;
import com.building_security_system.service.FacilityService;
import com.building_security_system.util.CommandManager;
import com.building_security_system.util.TestingThread;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@Log4j2
@CrossOrigin
public class SocketController {
    private FacilityService facilityService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // Maps to track facility states
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityStopFlags = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityPauseFlags = new ConcurrentHashMap<>();

    @Autowired
    public SocketController(SocketIOServer socketServer, FacilityService facilityService) {
        this.facilityService = facilityService;

        socketServer.addConnectListener(onUserConnectWithSocket);
        socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        socketServer.addEventListener("testing-system", SocketCommandDto.class, onSendMessage);
        socketServer.addEventListener("stop-resume-testing", SocketCommandDto.class, onStopResume);
    }

    public ConnectListener onUserConnectWithSocket = client ->
            log.info("Client connected: {}", client.getSessionId());

    public DisconnectListener onUserDisconnectWithSocket = client ->
            log.info("Client disconnected: {}", client.getSessionId());

    public DataListener<SocketCommandDto> onStopResume = (client, command, ackRequest) -> {
        String[] parts = command.getContents().split(":");
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

    public DataListener<SocketCommandDto> onSendMessage = (client, facilityId, ackRequest) -> {
        long fId = Long.parseLong(facilityId.getContents());
        AtomicBoolean stopFlag = facilityStopFlags.computeIfAbsent(fId, id -> new AtomicBoolean(false));
        AtomicBoolean pauseFlag = facilityPauseFlags.computeIfAbsent(fId, id -> new AtomicBoolean(false));

        Thread thread = new Thread(TestingThread.builder()
                .counter(0)
                .ackRequest(ackRequest)
                .executorService(executorService)
                .commandManager(new CommandManager())
                .client(client)
                .facilityService(facilityService)
                .facilityId(fId)
                .stopFlag(stopFlag)
                .pauseFlag(pauseFlag)
                .build());

        thread.start();
    };
}