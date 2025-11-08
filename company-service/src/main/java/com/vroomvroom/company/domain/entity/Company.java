package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "company_manager_id"))
    private CompanyManagerId companyManagerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id"))
    private HubId hubId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "company_name"))
    private CompanyName companyName;

    @Embedded
    @AttributeOverride(name = "detail", column = @Column(name = "company_address"))
    private CompanyAddress companyAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type")
    private CompanyType companyType;

    private Company(
            HubId hubId,
            CompanyManagerId companyManagerId,
            CompanyName companyName,
            CompanyAddress companyAddress,
            CompanyType companyType
    ){
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyType = companyType;
    }

    public static Company create(
            HubId hubId,
            CompanyManagerId companyManagerId,
            CompanyName companyName,
            CompanyAddress companyAddress,
            CompanyType companyType
    ) {
        validate(hubId, companyManagerId, companyName, companyAddress, companyType);
        return new Company(hubId, companyManagerId, companyName, companyAddress, companyType);
    }

    private static void validate(
            HubId hubId,
            CompanyManagerId companyManagerId,
            CompanyName companyName,
            CompanyAddress companyAddress,
            CompanyType companyType
    ) {
        if (hubId == null || companyManagerId == null || companyName == null
                || companyAddress == null || companyType == null) {
            throw new CustomException(ErrorCode.MISSING_REQUIRED_FIELD);
        }
    }
}
