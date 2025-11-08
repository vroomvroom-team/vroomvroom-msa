package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.application.port.UserClient;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.domain.service.CompanyDomainService;
import com.vroomvroom.company.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyDomainService companyDomainService;
    private final CompanyRepository companyRepository;

    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public UUID createCompany(CreateCompanyCommand command) {
        log.info("업체 생성 시작");

        CompanyType companyType = parseCompanyType(command.companyType());

        HubId hubId = HubId.of(command.hubId());
        validateHub(hubId);

        CompanyManagerId companyManagerId = CompanyManagerId.of(command.companyManagerId());
        validateManager(companyManagerId);

        CompanyName companyName = CompanyName.of(command.companyName());
        companyDomainService.validateDuplicateCompanyName(companyName);

        CompanyAddress companyAddress = CompanyAddress.of(command.companyAddress());
        companyDomainService.validateDuplicateCompanyAddress(companyAddress);

        Company company = Company.create(hubId, companyManagerId, companyName, companyAddress, companyType);

        Company saveCompany = companyRepository.save(company);

        log.info("업체 생성 완료: companyId = {}", saveCompany.getCompanyId());
        return saveCompany.getCompanyId();
    }

    private CompanyType parseCompanyType(String type) {
        try {
            return CompanyType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_TYPE);
        }
    }

    private void validateHub(HubId hubId) {
        boolean exists = hubClient.existsHub(hubId);
        if (!exists) throw new CustomException(ErrorCode.HUB_NOT_FOUND);
    }

    private void validateManager(CompanyManagerId companyManagerId) {
        boolean exists = userClient.existsUser(companyManagerId);
        if (!exists) throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }
}
