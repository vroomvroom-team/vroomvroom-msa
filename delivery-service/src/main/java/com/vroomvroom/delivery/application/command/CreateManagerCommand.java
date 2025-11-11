package com.vroomvroom.delivery.application.command;

import com.vroomvroom.delivery.presentation.dto.request.CreateManagerReq;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateManagerCommand {

    private final Long userId;
    private final UUID hubId;
    private final String type;

    public static CreateManagerCommand from(CreateManagerReq request) {
        return new CreateManagerCommand(
            request.getUserId(),
            request.getHubId(),
            request.getType()
        );
    }
}
