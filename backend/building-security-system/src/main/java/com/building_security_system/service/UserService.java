package com.building_security_system.service;

import com.building_security_system.dto.LoginDto;
import com.building_security_system.models.User;

public interface UserService {

    User findByEmail(String email);
    User saveUser(User user);

}