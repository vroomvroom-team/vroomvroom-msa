package com.vroomvroom.delivery.domain.port;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentQueuePort {

    Optional<Long> popCompanyManagerSequence(UUID hubId);

    void pushCompanyManagerSequence(UUID hubId, Long sequence);
}
