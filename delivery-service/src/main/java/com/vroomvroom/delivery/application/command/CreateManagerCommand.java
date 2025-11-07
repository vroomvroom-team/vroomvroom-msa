package com.vroomvroom.delivery.application.command;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateManagerCommand {

    private final Long userId;
    private final UUID hubId;
    private final String type;
}
