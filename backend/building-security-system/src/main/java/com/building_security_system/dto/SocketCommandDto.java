package com.building_security_system.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SocketCommandDto {
    long id;
    String command;
    String isRandom;
}
