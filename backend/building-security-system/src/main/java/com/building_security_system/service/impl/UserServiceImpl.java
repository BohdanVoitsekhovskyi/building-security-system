package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.UserRepository;


import com.building_security_system.models.Role;
import com.building_security_system.models.User;
import com.building_security_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = User.toModel(userRepository.findOneByUsername(username));
        if (user == null) {
            throw new UsernameNotFoundException("User with username - " + username + " not found");
        }
        user.setRoles(new ArrayList<>());
        user.getRoles().add(Role.USER);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.name()))
        );

        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), authorities);

    }
    @Override
    public User findByUsername(String username) {
        return User.toModel(userRepository.findOneByUsername(username)) ;

    }
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return User.toModel(userRepository.save(User.toEntity(user))) ;
    }
}