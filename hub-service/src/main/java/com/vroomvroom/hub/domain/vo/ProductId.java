package com.vroomvroom.hub.domain.vo;

import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
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
public class ProductId {
    private UUID productId;

    private ProductId(UUID productId) {
        if (productId == null) throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        this.productId = productId;
    }

    public static ProductId of(UUID productId) {
        return new ProductId(productId);
    }
}