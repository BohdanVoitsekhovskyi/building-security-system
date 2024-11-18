package com.building_security_system.service;

import com.building_security_system.dto.LoginDto;
import com.building_security_system.dto.SignUpDTO;
import com.building_security_system.models.User;

public interface UserService {
    User logIn(LoginDto loginDto);
    boolean signUp(User user);
}