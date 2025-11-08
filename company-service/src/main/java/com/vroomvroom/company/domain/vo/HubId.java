package com.vroomvroom.company.domain.vo;

import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HubId {

    private UUID id;

    private HubId(UUID id) {
        if(id == null) throw new CustomException(ErrorCode.HUB_ID_REQUIRED);
        this.id = id;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
