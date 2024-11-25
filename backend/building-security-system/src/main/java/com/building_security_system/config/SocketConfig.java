package com.building_security_system.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE)
public class SocketConfig {
    @Value("${server.address}")
    String socketHost;

    @Value("${socket.port}")
    Integer socketPort;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname(socketHost);
        configuration.setPort(socketPort);
        configuration.setJsonSupport(new JacksonJsonSupport(new JavaTimeModule()));

        SocketIOServer server = new SocketIOServer(configuration);
        server.start();

        return server;
    }
}