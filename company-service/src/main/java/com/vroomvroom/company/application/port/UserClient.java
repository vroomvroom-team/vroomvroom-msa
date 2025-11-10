package com.vroomvroom.company.application.port;

import java.util.Optional;
import java.util.UUID;

public interface UserClient {
    boolean existsUser(Long companyManagerId);
    Optional<UUID> getHubIdByUserId(Long userId);
}
