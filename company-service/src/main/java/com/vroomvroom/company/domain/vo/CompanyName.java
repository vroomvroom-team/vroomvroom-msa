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
public class CompanyName {

    private String value;

    private CompanyName(String value) {
        if (value == null || value.isBlank()) throw new CustomException(ErrorCode.COMPANY_NAME_REQUIRED);
        this.value = value;
    }

    public static CompanyName of(String value) {
        return new CompanyName(value);
    }
}
