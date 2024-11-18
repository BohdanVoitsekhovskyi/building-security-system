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

    @PostMapping("login/{username}")
    public ResponseEntity<?> loginUser(@PathVariable String username) {
        System.out.println(username);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(username));
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
                String username = decodedJWT.getSubject();
                User user = userService.findByUsername(username);

                String access_token = jwtService.createAccessToken(username,
                        request.getRequestURL().toString(),
                        user.getRoles().stream().map(Role::toString).toList());

                HashMap<String, String> tokenMap = new HashMap<>();
                tokenMap.put("access_token", access_token);
                System.out.println(access_token);
                tokenMap.put("refresh_token", token);
                System.out.println(token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokenMap);
            }
            System.out.println(authorizationToken);
        }
        catch (Exception exception) {
            response.setHeader("Error", exception.getMessage());
            response.setStatus(403);
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO user) {
        System.out.println(user);
        if(userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(400).body("User " + user.getUsername() + " has account");
        }
        String fullName = user.getFirstName() + ' ' + user.getLastName();
        User newUser = User.builder()
                .name(fullName)
                .id(System.currentTimeMillis())
                .roles(List.of(Role.USER))
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        System.out.println(newUser);
        return new ResponseEntity<>(userService.saveUser(newUser) , HttpStatus.CREATED);
    }
}