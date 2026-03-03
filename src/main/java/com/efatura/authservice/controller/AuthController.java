package com.efatura.authservice.controller;



import com.efatura.authservice.dto.RequestLogin;
import com.efatura.authservice.dto.RequestRefreshToken;
import com.efatura.authservice.dto.RequestRegister;
import com.efatura.authservice.dto.ResponseAuth;
import com.efatura.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseAuth register(@Valid @RequestBody RequestRegister requestRegister){
        return authService.register(requestRegister.email(), requestRegister.password(), requestRegister.fullName());
    }

    @PostMapping("/login")
    public ResponseAuth login(@Valid @RequestBody RequestLogin requestLogin){
        return authService.login(requestLogin.email(), requestLogin.password());
    }

    @PostMapping("/refresh")
    public ResponseAuth refreshToken(@Valid @RequestBody RequestRefreshToken requestRefreshToken){
        return authService.refreshToken(requestRefreshToken.refreshToken());
    }
}
