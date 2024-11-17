package com.building_security_system.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.building_security_system.dto.SignUpDTO;
import com.building_security_system.models.Role;
import com.building_security_system.models.User;
import com.building_security_system.security.JWTService;
import com.building_security_system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final JWTService jwtService;

    @Autowired
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("login/{email}")
    public ResponseEntity<Object> loginUser(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
                String token = authorizationToken.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("catSecret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String email = decodedJWT.getSubject();
                User user = userService.findByEmail(email);

                String access_token = jwtService.createAccessToken(email,
                        request.getRequestURL().toString(),
                        user.getRoles().stream().map(Role::toString).toList());

                HashMap<String, String> tokenMap = new HashMap<>();
                tokenMap.put("access_token", access_token);
                tokenMap.put("refresh_token", token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);
            }
        }
        catch(Exception exception) {
            response.setHeader("Error", exception.getMessage());
            response.setStatus(403);
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO user) {
        if(userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(400).body("User " + user.getEmail() + " has account");
        }
        String fullName = user.getFirstName() + ' ' + user.getLastName();
        User newUser = User.builder()
                .name(fullName)
                .id(System.currentTimeMillis())
                .roles(List.of(Role.USER))
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        return new  ResponseEntity<>(userService.saveUser(newUser) , HttpStatus.CREATED);
    }



}