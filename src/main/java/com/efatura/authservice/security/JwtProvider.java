package com.efatura.authservice.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey(){

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateAccessToken(com.efatura.authservice.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("companyId", user.getCompanyId());
        claims.put("roles", user.getRoles());

        Long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)//company id ve rolü claimliyor
                .subject(user.getEmail())//kime ait
                .issuedAt(new Date())//oluşturulma zamani
                .expiration(new Date(now + jwtProperties.expirationMs()))//bitiş zamani
                .signWith(getSigningKey())//digital sign
                .compact();
    }


    public String generateRefreshToken(com.efatura.authservice.model.User user){
        Long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(now + jwtProperties.refreshexpirationms()))
                .signWith(getSigningKey())
                .compact();

    }


    public String getEmailFromToken(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public boolean validateToken(String token){

        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(JwtException | IllegalArgumentException e) {
            return false;

        }
    }


}
