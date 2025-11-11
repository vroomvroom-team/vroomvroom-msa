package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.vo.CompanyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_company")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "company_manager_id", nullable = false)
    private Long companyManagerId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type")
    private CompanyType companyType;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public static Company create(
            UUID hubId,
            Long companyManagerId,
            String companyName,
            String companyAddress,
            CompanyType companyType
    ) {
        validate(hubId, companyManagerId, companyName, companyAddress, companyType);
        return Company.builder()
                .hubId(hubId)
                .companyManagerId(companyManagerId)
                .companyName(companyName)
                .companyAddress(companyAddress)
                .companyType(companyType)
                .build();
    }

    private static void validate(
            UUID hubId,
            Long companyManagerId,
            String companyName,
            String companyAddress,
            CompanyType companyType
    ) {
        validateHub(hubId);
        validateManager(companyManagerId);
        validateCompanyName(companyName);
        validateCompanyAddress(companyAddress);
        validateCompanyType(companyType);
    }

    private static void validateHub(UUID hubId) {
        if (hubId == null) throw new CustomException(ErrorCode.HUB_ID_REQUIRED);
    }

    private static void validateManager(Long companyManagerId) {
        if (companyManagerId == null) throw new CustomException(ErrorCode.COMPANY_MANAGER_REQUIRED);
    }

    private static void validateCompanyName(String companyName) {
        if (companyName == null || companyName.isBlank())
            throw new CustomException(ErrorCode.COMPANY_NAME_REQUIRED);
    }

    private static void validateCompanyAddress(String companyAddress) {
        if (companyAddress == null || companyAddress.isBlank())
            throw new CustomException(ErrorCode.COMPANY_ADDRESS_REQUIRED);
    }

    private static void validateCompanyType(CompanyType companyType) {
        if (companyType == null) throw new CustomException(ErrorCode.COMPANY_TYPE_REQUIRED);
    }

    public void changeManager(Long companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public void changeName(String companyName) {
        validateCompanyName(companyName);
        this.companyName = companyName;
    }

    public void changeAddress(String companyAddress) {
        validateCompanyAddress(companyAddress);
        this.companyAddress = companyAddress;
    }

    public void changeType(CompanyType companyType) {
        this.companyType = companyType;
    }
}

