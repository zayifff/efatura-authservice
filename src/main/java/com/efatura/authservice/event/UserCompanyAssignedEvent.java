package com.efatura.authservice.event;

public record UserCompanyAssignedEvent(
        String email,
        Long companyId,
        String role
) {}