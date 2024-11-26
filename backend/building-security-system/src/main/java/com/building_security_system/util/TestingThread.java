package com.building_security_system.util;

import com.building_security_system.dto.DetectorReactionDto;
import com.building_security_system.dto.SystemReactionDto;
import com.building_security_system.models.Facility;
import com.building_security_system.models.SystemReaction;
import com.building_security_system.service.FacilityService;
import com.building_security_system.service.LoggerService;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestingThread implements Runnable {
    private long facilityId;
    private SocketIOClient client;
    private AckRequest ackRequest;
    private CommandManager commandManager;
    private FacilityService facilityService;
    private AtomicBoolean stopFlag;
    private AtomicBoolean pauseFlag;
    private LoggerService loggerService;

    private boolean isRandom;

    @Override
    public void run() {
        loggerService.eraseLogs(facilityId);
        System.out.println("Log erased" + facilityId);
        System.out.println("Requested floors list of facility with id: " + facilityId);
        Facility facility = facilityService.getFacilityById(facilityId);

        if (facility == null) {
            client.sendEvent("error", "Invalid facility ID: " + facilityId);
            return;
        }

        commandManager.createCommands(facility.getFloors()
                .stream()
                .flatMap(floor -> floor.getDetectors().stream())
                .collect(Collectors.toList()),isRandom);

        try {
            while (!stopFlag.get()) {
                if (pauseFlag.get()) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    Thread.sleep(500); // Wait briefly while paused
                    continue;
                }

                SystemReaction systemReaction = commandManager.invokeCommands();

                if (systemReaction == null) {
                    break;
                }

                SystemReactionDto dto = new SystemReactionDto(systemReaction.getDetectorsReaction().stream().map(DetectorReactionDto::toDto).toList());

                client.sendEvent("floorsList", dto);
                loggerService.log(systemReaction,facilityId);

                int seconds = Randomizer.getRandomNumber(3,7) * 1000;

                try {
                    Thread.sleep(seconds);
                } catch (InterruptedException e) {
                    break;
                }
            }

            pauseFlag.set(false);
            stopFlag.set(false);

            System.out.println("Processing terminated for facility: " + facilityId);
        } catch (InterruptedException e) {
            System.out.println("Processing interrupted for facility: " + facilityId);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}