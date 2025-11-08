package com.vroomvroom.company.domain.vo;

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

    public CompanyAddress(String detail) {
        this.detail = detail;
    }
}
