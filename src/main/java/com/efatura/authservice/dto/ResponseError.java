package com.efatura.authservice.dto;

import java.time.LocalDateTime;

public record ResponseError(Integer status, String message, LocalDateTime timestamp) {

}
