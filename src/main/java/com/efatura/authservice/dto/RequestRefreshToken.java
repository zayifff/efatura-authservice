package com.efatura.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestRefreshToken(@NotBlank String refreshToken) {
}
