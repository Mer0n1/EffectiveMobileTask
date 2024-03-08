package com.example.effectivemobiletask.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.effectivemobiletask.models.BankAccount;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtService {

    public static String secret;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        secret = environment.getProperty("SECRET");
    }

    public String generateToken(BankAccount account) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", account.getUsername())
                .withClaim("password", account.getUsername())
                .withClaim("id", account.getId())
                .withIssuedAt(new Date())
                .withIssuer("TODO")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }
}