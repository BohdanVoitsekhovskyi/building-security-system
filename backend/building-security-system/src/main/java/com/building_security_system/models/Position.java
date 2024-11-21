package com.building_security_system.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Position {
    private double x;
    private double y;
}