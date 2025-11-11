package com.vroomvroom.company.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateProductReq (
       @NotNull(message = "업체 ID는 필수입니다.")
       UUID companyId,

       @NotNull(message = "허브 ID는 필수입니다.")
       UUID hubId,

       @NotBlank(message = "상품명은 필수입니다.")
       String productName,

       @NotNull(message = "상품 가격은 필수입니다.")
       @PositiveOrZero
       Long price
) {}
