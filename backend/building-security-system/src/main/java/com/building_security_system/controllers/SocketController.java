package com.building_security_system.controllers;

import com.building_security_system.dto.SocketCommandDto;
import com.building_security_system.service.FacilityService;
import com.building_security_system.service.LoggerService;
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
    private LoggerService loggerService;
    private final ConcurrentHashMap<String, Thread> clientThreads = new ConcurrentHashMap<>();

    // Maps to track facility states
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityStopFlags = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, AtomicBoolean> facilityPauseFlags = new ConcurrentHashMap<>();

    @Autowired
    public SocketController(SocketIOServer socketServer, FacilityService facilityService, LoggerService loggerService) {
        this.facilityService = facilityService;
        this.loggerService = loggerService;

        socketServer.addConnectListener(onUserConnectWithSocket);
        socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        socketServer.addEventListener("testing-system", SocketCommandDto.class, onSendMessage);
        socketServer.addEventListener("stop-resume-testing", SocketCommandDto.class, onStopResume);
    }

    public ConnectListener onUserConnectWithSocket = client ->
            log.info("Client connected: {}", client.getSessionId());

    public DisconnectListener onUserDisconnectWithSocket = client -> {
        log.info("Client disconnected: {}", client.getSessionId());


        Thread thread = clientThreads.remove(client.getSessionId().toString());
        if (thread != null) {
            log.info("Stopping thread for client: {}", client.getSessionId());
            thread.interrupt();
        }
    };


    public DataListener<SocketCommandDto> onStopResume = (client, command, ackRequest) -> {
        String action = command.getCommand().toUpperCase();
        long facilityId = command.getId();

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

    public DataListener<SocketCommandDto> onSendMessage = (client, socketCommandDto, ackRequest) -> {
        System.out.println(socketCommandDto);
        long fId = socketCommandDto.getId();

        AtomicBoolean stopFlag = facilityStopFlags.computeIfAbsent(fId, id -> new AtomicBoolean(false));
        AtomicBoolean pauseFlag = facilityPauseFlags.computeIfAbsent(fId, id -> new AtomicBoolean(false));

        stopFlag.set(false);
        pauseFlag.set(false);

        Thread thread = new Thread(TestingThread.builder()
                .ackRequest(ackRequest)
                .commandManager(new CommandManager())
                .client(client)
                .facilityService(facilityService)
                .facilityId(fId)
                .stopFlag(stopFlag)
                .pauseFlag(pauseFlag)
                .loggerService(loggerService)
                .isRandom(Boolean.parseBoolean(socketCommandDto.getIsRandom()))
                .build());

        clientThreads.put(client.getSessionId().toString(), thread);
        thread.start();
    };

}