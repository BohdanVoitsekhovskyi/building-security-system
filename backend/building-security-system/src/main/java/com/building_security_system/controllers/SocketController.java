package com.building_security_system.controllers;

import com.building_security_system.models.Facility;
import com.building_security_system.service.FacilityService;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Log4j2
public class SocketController {
    private final FacilityService facilityService;
    private final ExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Autowired
    SocketController(SocketIOServer socketServer, FacilityService facilityService) {
        this.facilityService = facilityService;

        socketServer.addConnectListener(onUserConnectWithSocket);
        socketServer.addDisconnectListener(onUserDisconnectWithSocket);
        socketServer.addEventListener("messageSendToUser", Long.class, onSendMessage);
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

            executorService.execute(() -> {
                try {
                    Thread.sleep((int)(Math.random() * 5000) + 500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.sendEvent("floorsList", facility.getFloors());
            });

            ackRequest.sendAckData("Request will now be processed");
        }
    };
}