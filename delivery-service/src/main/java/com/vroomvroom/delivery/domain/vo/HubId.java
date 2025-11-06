package com.vroomvroom.delivery.domain.vo;

import java.util.UUID;

public class HubId {
    
    private UUID id;
    private HubId(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("유효하지 않는 허브ID 입니다.");
        }
        this.id = id;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
