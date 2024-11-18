package com.building_security_system.service.impl;

import com.building_security_system.db_access.entities.UserEntity;
import com.building_security_system.db_access.repositories.UserRepository;
import com.building_security_system.dto.LoginDto;
import com.building_security_system.dto.SignUpDTO;
import com.building_security_system.models.User;
import com.building_security_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User logIn(LoginDto loginDto) {
        return User.toModel(userRepository.findOneByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword())) ;
    }

    @Override
    public boolean signUp( User user) {
        boolean isUserAlreadyExist = userRepository.findByEmail(user.getEmail()) != null;
        if (isUserAlreadyExist) {
            return false;
        }
       return User.toModel(userRepository.save(User.toEntity(user))) != null;
    }
}