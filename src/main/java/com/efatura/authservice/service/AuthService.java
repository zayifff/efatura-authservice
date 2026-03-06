package com.efatura.authservice.service;


import com.efatura.authservice.dto.ResponseAuth;
import com.efatura.authservice.model.User;
import com.efatura.authservice.repository.UserRepository;
import com.efatura.authservice.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

//refreshToken: Gelen refresh token ile kullanıcıyı bul, token geçerli mi kontrol et, yeni access token üret, ResponseAuth dön.

    public ResponseAuth register(String email, String password, String fullName){
        if(userRespository.existsByEmail(email)){
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = userRespository.save(User.builder().email(email).password(hashedPassword).fullName(fullName).roles(Set.of("ROLE_USER")).build());
        String refreshToken = jwtProvider.generateRefreshToken(newUser);
        newUser.setRefreshToken(refreshToken);
        newUser = userRespository.save(newUser);

        return new ResponseAuth(jwtProvider.generateAccessToken(newUser), refreshToken,newUser.getEmail(),newUser.getRoles());
    }

    public ResponseAuth login(String email, String password){

        authenticationManager.authenticate(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(email, password));
        User user = userRespository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String refreshToken = jwtProvider.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        user = userRespository.save(user);

        return new ResponseAuth(jwtProvider.generateAccessToken(user), refreshToken,user.getEmail(),user.getRoles());
    }

    public ResponseAuth refreshToken(String refreshToken){
        String email = jwtProvider.getEmailFromToken(refreshToken);
        User user = userRespository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(!user.getRefreshToken().equals(refreshToken)){
            throw new RuntimeException("Invalid refresh token");
        }
        String accessToken = jwtProvider.generateAccessToken(user);

        return new ResponseAuth(accessToken, refreshToken,user.getEmail(),user.getRoles());
    }

}
