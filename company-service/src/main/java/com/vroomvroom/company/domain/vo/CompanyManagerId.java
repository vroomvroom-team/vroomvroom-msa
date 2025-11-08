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
public class CompanyManagerId {

    private Long id;

    private CompanyManagerId(Long id) {
        if(id == null || id <= 0) throw new CustomException(ErrorCode.COMPANY_MANAGER_REQUIRED);
        this.id = id;
    }

    public static CompanyManagerId of(Long id) {
        return new CompanyManagerId(id);
    }
}
