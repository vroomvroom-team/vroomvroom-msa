package com.vroomvroom.delivery.domain.port;

import java.util.UUID;

public interface UserClient {

    void verifyUserHasRole(Long userId, String role);

    UUID getUserSlackId(Long receiverId);
}
