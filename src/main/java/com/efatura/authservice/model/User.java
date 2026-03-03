package com.efatura.authservice.model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String fullName;
    private Set<String> roles;
    private Long companyId;
    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
