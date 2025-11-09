package com.vroomvroom.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateManagerReq {

    @NotNull
    private Long userId;

    private UUID hubId;

    @NotNull
    private String type;
}
