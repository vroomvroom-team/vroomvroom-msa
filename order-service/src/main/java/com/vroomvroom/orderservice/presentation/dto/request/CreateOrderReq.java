package com.vroomvroom.orderservice.presentation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderReq {
    @NotNull(message = "공급 업체 ID는 필수입니다")
    private UUID supplyCompanyId;

    @NotNull(message = "수령 업체 ID는 필수입니다")
    private UUID receiveCompanyId;

    @NotNull(message = "상품 ID는 필수입니다")
    private UUID productId;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Long quantity;

    @NotNull(message = "납기일은 필수입니다")
    @Future(message = "납기일은 현재 시간 이후여야 합니다")
    private LocalDateTime deadline;

    @Size(max = 500, message = "요청 사항은 최대 500자까지 입력 가능합니다")
    private String requestNote;
}
