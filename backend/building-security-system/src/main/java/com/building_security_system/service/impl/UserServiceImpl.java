package com.building_security_system.service.impl;

import com.building_security_system.db_access.entities.UserEntity;
import com.building_security_system.db_access.repositories.UserRepository;
import com.building_security_system.dto.LoginDto;
import com.building_security_system.dto.SignUpDTO;
import com.building_security_system.models.User;
import com.building_security_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User logIn(LoginDto loginDto) {
        User user = User.toModel(userRepository.findByEmail(loginDto.getEmail())) ;
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            return user;
       return null;
    }

    @Override
    public boolean signUp( User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean isUserAlreadyExist = userRepository.findByEmail(user.getEmail()) != null;
        if (isUserAlreadyExist) {
            return false;
        }
       return User.toModel(userRepository.save(User.toEntity(user))) != null;
    }
}