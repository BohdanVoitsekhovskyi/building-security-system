package com.building_security_system.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTService {

    @Value("${jwt.token.secret}")
    private String secret;

    public JWTService() {

    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }

    public String createAccessToken(String username, String requestURL, List<String> roles) {
        System.out.println("In create access token");
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .withIssuer(requestURL)
                .withClaim("roles", roles)
                .sign(algorithm);

    }

    public String createRefreshToken(String username, String requestURL, List<String> roles) {
        System.out.println("In create refresh token");
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(requestURL)
                .withClaim("roles", roles)
                .sign(algorithm);
    }
}

