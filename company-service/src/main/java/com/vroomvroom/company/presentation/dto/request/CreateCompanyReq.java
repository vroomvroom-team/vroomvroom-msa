package com.vroomvroom.company.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCompanyReq (
        @NotNull(message = "허브 ID는 필수입니다.")
        UUID hubId,

        @NotNull(message = "업체 관리자 ID는 필수입니다.")
        Long companyManagerId,

        @NotBlank(message = "업체 이름은 필수입니다.")
        String companyName,

        @NotBlank(message = "업체 주소는 필수입니다.")
        String companyAddress,

        @NotBlank(message = "업체 타입은 필수입니다.")
        String companyType
) {}
