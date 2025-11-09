package com.vroomvroom.orderservice.presentation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderReq {
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    private Long quantity;

    @Future(message = "납기일은 현재 시간 이후여야 합니다")
    private LocalDateTime deadline;

    @Size(max = 500, message = "요청 사항은 최대 500자까지 입력 가능합니다")
    private String requestNote;
}
