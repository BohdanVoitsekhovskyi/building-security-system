package com.building_security_system.controllers;

import com.building_security_system.dto.LoginDto;
import com.building_security_system.dto.SignUpDTO;
import com.building_security_system.models.Facility;
import com.building_security_system.models.User;
import com.building_security_system.service.FacilityService;
import com.building_security_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final FacilityService facilityService;

    @Autowired
    public UserController(UserService userService, FacilityService facilityService) {
        this.userService = userService;
        this.facilityService = facilityService;
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> logIn(@RequestBody LoginDto user) {
        User loginUser = userService.logIn(user);
        if(loginUser == null) {
            return new ResponseEntity<>("Error during login user(user doesn`t exist)", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(loginUser);
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> signUp( @RequestBody SignUpDTO signUpDTO){
        long id = System.currentTimeMillis();
        User newUser = User.builder()
                .id(id)
                .email(signUpDTO.getEmail())
                .password(signUpDTO.getPassword())
                .firstname(signUpDTO.getFirstname())
                .lastname(signUpDTO.getLastname())
                .build();

        boolean successSignUp = userService.signUp(newUser);

        if(successSignUp) {
            Facility facility = Facility.builder()
                            .id(id)
                            .floors(new ArrayList<>())
                            .build();

            facilityService.saveFacility(facility);
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}