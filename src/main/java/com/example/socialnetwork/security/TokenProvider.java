package com.example.socialnetwork.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private final String key;
    private final Integer exireMins;

    public TokenProvider(@Value("${jwt.secret}") String key, @Value("${jwt.expireMins}") Integer exireMins) {
        this.key = key;
        this.exireMins = exireMins;
    }


    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC512(key.getBytes());
        JWTCreator.Builder builder = JWT.create()
                .withSubject(String.valueOf(((UserDetails)authentication.getPrincipal()).getUsername()));

        return builder
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(exireMins).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        try {
            DecodedJWT decodedToken = JWT.require(Algorithm.HMAC512(key))
                    .build()
                    .verify(token);

            String userId = decodedToken.getSubject();

            return new UsernamePasswordAuthenticationToken(Long.parseLong(userId), token, null);
        } catch (Exception e) {
            log.warn("Failed to validate jwt", e);
            return null;
        }
    }

}
