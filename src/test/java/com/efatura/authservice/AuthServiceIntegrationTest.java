package com.efatura.authservice;


import com.efatura.authservice.dto.ResponseAuth;
import com.efatura.authservice.repository.UserRepository;
import com.efatura.authservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerSuccess() {
        ResponseAuth responseAuth = authService.register("test@test1.com", "123456", "Test User");

        assertNotNull(responseAuth.accessToken());
        assertNotNull(responseAuth.refreshToken());
        assertEquals("test@test1.com" , responseAuth.email());
        assertEquals("Bearer" , responseAuth.tokenType());
        assertTrue(responseAuth.roles().contains("ROLE_USER"));

    }

    @Test
    void registerDuplicateEmail() {
        authService.register("test@test1.com", "123456", "Test User");
        assertThrows(RuntimeException.class, () -> {
            authService.register("test@test1.com", "654321", "Test User 2");
        });
    }

    @Test
    void sucessfulLogin(){

        authService.register("test@test1.com" , "123456" , "Test user");
        ResponseAuth responseAuth = authService.login("test@test1.com", "123456");
        assertNotNull(responseAuth.accessToken());
        assertNotNull(responseAuth.refreshToken());
        assertEquals("test@test1.com" , responseAuth.email());
        assertEquals("Bearer" , responseAuth.tokenType());
        assertTrue(responseAuth.roles().contains("ROLE_USER"));

    }

    @Test
    void failedLoginByPassword(){
        authService.register("test@test1.com" , "123456" , "Test user");

        assertThrows(Exception.class, () -> {
            authService.login("test@test1.com", "wrongPassword");
        });
    }

    @Test
    void failedLoginByEmailNotFound(){
        authService.register("test@test1.com" , "123456" , "Test user");

        assertThrows(Exception.class, () -> {
            authService.login("test1@test1.com", "123456");
        });
    }

    @Test
    void refreshTokenSuccess(){

        ResponseAuth responseAuth = authService.register("test@test1.com" , "123456" , "Test user");
        ResponseAuth responseAuth2 = authService.refreshToken(responseAuth.refreshToken());
        assertNotNull(responseAuth2.accessToken());
        assertNotNull(responseAuth2.refreshToken());
        assertEquals(responseAuth.email(),responseAuth2.email());
        assertEquals("Bearer" , responseAuth2.tokenType());
        assertTrue(responseAuth2.roles().contains("ROLE_USER"));

    }

    @Test
    void refreshTokenFailed(){
        assertThrows(Exception.class, () -> {
            authService.refreshToken("wrongToken");
        });
    }






}
