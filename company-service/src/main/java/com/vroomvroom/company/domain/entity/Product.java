package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    Company company;


    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false)
    private Long price;

    public static Product create(
            Company company,
            UUID hubId,
            String productName,
            Long price
    ) {
        validate(company, hubId, productName, price);

        return Product.builder()
                .company(company)
                .hubId(hubId)
                .productName(productName)
                .price(price)
                .build();
    }

    private static void validate(
            Company company,
            UUID hubId,
            String productName,
            Long price
    ) {
        validateHub(company, hubId);
        validateProductName(productName);
        validatePrice(price);
    }

    private static void validateHub(Company company, UUID hubId) {
        if (!company.getHubId().equals(hubId)) throw new CustomException(ErrorCode.HUB_MISMATCH);
    }

    private static void validateProductName(String productName) {
        if (productName == null || productName.isBlank()) throw new CustomException(ErrorCode.DUPLICATE_PRODUCT_NAME);
    }

    private static void validatePrice(Long price) {
        if (price == null || price <= 0) throw new CustomException(ErrorCode.INVALID_PRICE);
    }

    public void changeProductName(String productName) {
        validateProductName(productName);
        this.productName = productName;
    }

    public void changePrice(Long price) {
        validatePrice(price);
        this.price = price;
    }
}
