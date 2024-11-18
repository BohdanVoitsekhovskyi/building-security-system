package com.building_security_system.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginDto {
    private String username;
    private String password;
}