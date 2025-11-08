package com.vroomvroom.company.domain.vo;

import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CompanyAddress {

    private String detail;

    private CompanyAddress(String detail) {
        if (detail == null || detail.isBlank()) throw new CustomException(ErrorCode.COMPANY_ADDRESS_REQUIRED);
        this.detail = detail;
    }

    public static CompanyAddress of(String detail) {
        return new CompanyAddress(detail);
    }
}
