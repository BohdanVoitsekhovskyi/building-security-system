package com.building_security_system.service;

import com.building_security_system.models.User;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
}