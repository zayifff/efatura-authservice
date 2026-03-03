package com.efatura.authservice;

import com.efatura.authservice.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

}
