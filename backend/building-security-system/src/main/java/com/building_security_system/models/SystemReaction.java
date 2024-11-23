package com.building_security_system.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemReaction {
    private List<Detector> detectors;
    private String systemAnswer;
    private LocalDateTime reactionTime = LocalDateTime.now();
}
