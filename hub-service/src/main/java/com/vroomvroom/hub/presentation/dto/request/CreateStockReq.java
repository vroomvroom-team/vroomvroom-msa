package com.vroomvroom.hub.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStockReq {
    private UUID hubId;
    private UUID productId;
    private Long quantity;
}