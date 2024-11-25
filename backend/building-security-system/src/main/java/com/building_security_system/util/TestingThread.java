package com.building_security_system.util;

import com.building_security_system.models.Facility;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.FacilityService;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestingThread implements Runnable {
    private int counter;
    private long facilityId;
    private SocketIOClient client;
    private AckRequest ackRequest;
    private CommandManager commandManager;
    private FacilityService facilityService;
    private ExecutorService executorService;
    private AtomicBoolean stopFlag;
    private AtomicBoolean pauseFlag;

    @Override
    public void run() {
        System.out.println("Requested floors list of facility with id: " + facilityId);
        Facility facility = facilityService.getFacilityById(facilityId);

        if (facility == null) {
            client.sendEvent("error", "Invalid facility ID: " + facilityId);
            return;
        }

        executorService.submit(() -> {
            commandManager.createCommands(facility.getFloors()
                    .stream()
                    .flatMap(floor -> floor.getDetectors().stream())
                    .collect(Collectors.toList()));

            try {
                while (!stopFlag.get()) {
                    if (pauseFlag.get()) {
                        Thread.sleep(5000); // Wait briefly while paused
                        continue;
                    }

                    SystemReaction systemReaction = commandManager.invokeCommands();

                    if (systemReaction == null) {
                        break;
                    }
                    client.sendEvent("floorsList", systemReaction);

                    Thread.sleep(1500); // Adjust delay as needed
                }

                pauseFlag.set(false);
                stopFlag.set(false);
                System.out.println("Processing terminated for facility: " + facilityId);
            } catch (InterruptedException e) {
                System.out.println("Processing interrupted for facility: " + facilityId);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
    }
}