package com.efatura.authservice.service;

import com.efatura.authservice.event.UserCompanyAssignedEvent;
import com.efatura.authservice.model.User;
import com.efatura.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = "user.company.assigned",
            groupId = "auth-service"
    )
    public void handleUserCompanyAssigned(UserCompanyAssignedEvent event) {
        log.info("Event Accepted → email: {}, companyId: {}, role: {}",
                event.email(), event.companyId(), event.role());

        User user = userRepository.findByEmail(event.email())
                .orElseThrow(() -> new RuntimeException("User not found: " + event.email()));

        user.setCompanyId(event.companyId());
        user.getRoles().add(event.role());
        userRepository.save(user);

        log.info("User updated → email: {}, companyId: {}, role: {}",
                event.email(), event.companyId(), event.role());
    }
}