package com.building_security_system.controllers;

import com.building_security_system.models.Facility;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.FacilityService;
import com.building_security_system.util.CommandManager;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class SocketController {
    private final FacilityService facilityService;
    private final ExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final CommandManager commandManager = new CommandManager();

    @Autowired
    SocketController(SocketIOServer socketServer, FacilityService facilityService) {
        this.facilityService = facilityService;

        socketServer.addConnectListener(onUserConnectWithSocket);
        socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        socketServer.addEventListener("testing-system", Long.class, onSendMessage);
    }

    public ConnectListener onUserConnectWithSocket = client -> {
        System.out.println("Hello from onConnect");
        log.info("Perform operation on user connect in controller");
    };

    public DisconnectListener onUserDisconnectWithSocket = client ->
            log.info("Perform operation on user disconnect in controller");

    public DataListener<Long> onSendMessage = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, Long facilityId, AckRequest ackRequest) {
            log.info("Requested floors list of facility with id: {}", facilityId);
            Facility facility = facilityService.getFacilityById(facilityId);

            commandManager.createCommands(facility
                    .getFloors()
                    .stream()
                    .flatMap(floor -> floor
                            .getDetectors()
                            .stream())
                    .collect(Collectors.toList()));
            List<SystemReaction> systemReactions = commandManager.invokeCommands();

            executorService.execute(() -> {
                try {
                    Thread.sleep((int) (Math.random() * 5000) + 500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.sendEvent("floorsList", systemReactions);
            });

            ackRequest.sendAckData("Request will now be processed");
        }
    };
}