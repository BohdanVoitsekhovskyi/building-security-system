package com.building_security_system.controllers;

import com.building_security_system.models.SystemReaction;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SocketController {
    @Autowired
    private SocketIOServer socketServer;

    SocketController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        this.socketServer.addEventListener("messageSendToUser", SystemReaction.class, onSendMessage);

    }


    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            System.out.println("Hello, my fucking friend");
            log.info("Perform operation on user connect in controller");
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
        }
    };

    public DataListener<SystemReaction> onSendMessage = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, SystemReaction systemReaction, AckRequest acknowledge) throws Exception {

            /**
             * Sending systemReaction to target user
             * target user should subscribe the socket event with his/her name.
             * Send the same payload to user
             */

            log.info("Detectors: " + systemReaction.getDetectors() + "\nSystem answer: " + systemReaction.getSystemAnswer() + "\nReaction time: " + systemReaction.getReactionTime());
//            socketServer.getBroadcastOperations().sendEvent()
            socketServer.getBroadcastOperations().sendEvent("frontend", client, systemReaction);

            System.out.println("Detectors: " + systemReaction.getDetectors());
            System.out.println("System answer: " + systemReaction.getSystemAnswer());
            System.out.println("Reaction time: " + systemReaction.getReactionTime());

            /**
             * After sending systemReaction to target user we can send acknowledge to sender
             */
            acknowledge.sendAckData("SystemReaction send to target user successfully");
        }
    };
}