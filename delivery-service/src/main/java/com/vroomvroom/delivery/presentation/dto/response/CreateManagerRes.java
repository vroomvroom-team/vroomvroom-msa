package com.vroomvroom.delivery.presentation.dto.response;

import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateManagerRes {

    private final Long userId;
    private final DeliveryManagerType type;
    private final UUID hubId;
    private final Long sequence;

    public static CreateManagerRes from(DeliveryManager manager) {
        UUID hubId =
            (manager.getType() == DeliveryManagerType.COMPANY_MANAGER && manager.getHubId() != null)
                ? manager.getHubId().getId() : null;

        return new CreateManagerRes(
            manager.getId(),
            manager.getType(),
            hubId,
            manager.getSequence().getValue()
        );
    }
}
