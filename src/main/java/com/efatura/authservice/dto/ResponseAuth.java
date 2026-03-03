package com.efatura.authservice.dto;

import java.util.Set;

public record ResponseAuth(String accessToken, String refreshToken , String tokenType , String email, Set<String> roles) {

    public ResponseAuth(String accessToken, String refreshToken, String email, Set<String> roles) {
        this(accessToken, refreshToken, "Bearer", email, roles);
    }

}
