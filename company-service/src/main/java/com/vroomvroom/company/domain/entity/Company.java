package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.vo.CompanyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_company")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @Builder(access = AccessLevel.PRIVATE)
    private Company(
            UUID hubId,
            Long companyManagerId,
            String companyName,
            String companyAddress,
            CompanyType companyType
    ){
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyType = companyType;
    }

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
        if (hubId == null) throw new CustomException(ErrorCode.HUB_ID_REQUIRED);
        if (companyManagerId == null) throw new CustomException(ErrorCode.COMPANY_MANAGER_REQUIRED);
        if (companyName == null || companyName.isBlank()) throw new CustomException(ErrorCode.COMPANY_NAME_REQUIRED);
        if (companyAddress == null || companyAddress.isBlank()) throw new CustomException(ErrorCode.COMPANY_ADDRESS_REQUIRED);
        if (companyType == null) throw new CustomException(ErrorCode.COMPANY_TYPE_REQUIRED);
    }

    public void changeManager(Long companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public void changeName(String companyName) {
        this.companyName = companyName;
    }

    public void changeAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void changeType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public void changeHub(UUID hubId) {
        this.hubId = hubId;
    }

    public void changeManager(Long companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public void changeName(String companyName) {
        this.companyName = companyName;
    }

    public void changeAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void changeType(CompanyType companyType) {
        this.companyType = companyType;
    }
}
