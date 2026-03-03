package com.efatura.authservice.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, Long expirationMs,
                             Long refreshexpirationms) {



}
