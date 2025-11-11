package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
            Long price,
            List<Product> existingProducts
    ) {
        validate(company, hubId, productName, price, existingProducts);

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
            Long price,
            List<Product> existingProducts
    ) {
        validateHub(company, hubId);
        validateName(productName);
        validatePrice(price);
        validateDuplicate(productName, existingProducts);
    }

    private static void validateHub(Company company, UUID hubId) {
        if (!company.getHubId().equals(hubId)) throw new CustomException(ErrorCode.HUB_MISMATCH);
    }

    private static void validateName(String productName) {
        if (productName == null || productName.isBlank()) throw new CustomException(ErrorCode.DUPLICATE_PRODUCT_NAME);
    }

    private static void validatePrice(Long price) {
        if (price == null || price <= 0) throw new CustomException(ErrorCode.INVALID_PRICE);
    }

    private static void validateDuplicate(String productName, List<Product> existingProducts) {
        boolean duplicate = existingProducts.stream()
                .anyMatch(p -> p.getProductName().equals(productName));

        if (duplicate) throw new CustomException(ErrorCode.DUPLICATE_PRODUCT_NAME);
    }
}
