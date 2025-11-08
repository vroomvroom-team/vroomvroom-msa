package com.vroomvroom.company.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
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
}
