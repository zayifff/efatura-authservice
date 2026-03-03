package com.efatura.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestLogin(@Email @NotBlank String email, @NotBlank String password) {
}
