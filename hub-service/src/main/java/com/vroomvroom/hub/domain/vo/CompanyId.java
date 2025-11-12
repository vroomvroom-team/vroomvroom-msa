package com.vroomvroom.hub.domain.vo;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.hub.exception.HubErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyId {
    private UUID companyId;

    private CompanyId(UUID companyId) {
        if (companyId == null) throw new CustomException(HubErrorCode.COMPANY_NOT_FOUND);
    }

    public static CompanyId of(UUID companyId) {
        return new CompanyId(companyId);
    }
}