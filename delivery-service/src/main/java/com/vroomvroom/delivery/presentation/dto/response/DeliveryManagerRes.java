package com.vroomvroom.delivery.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryManagerRes {

    private Long deliveryManagerId;
    private UUID hubId;
    private DeliveryManagerType type;
    private Long sequence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deletedAt;

    public static DeliveryManagerRes from(DeliveryManager deliveryManager) {
        return DeliveryManagerRes.builder()
                .deliveryManagerId(deliveryManager.getId())
                .hubId(deliveryManager.getHubId() != null
                        ? deliveryManager.getHubId().getId()
                        : null)
                .type(deliveryManager.getType())
                .sequence(deliveryManager.getSequence().getValue())
                .createdAt(deliveryManager.getCreatedAt())
                .updatedAt(deliveryManager.getUpdatedAt())
                .deletedAt(deliveryManager.getDeletedAt())
                .build();
    }
}