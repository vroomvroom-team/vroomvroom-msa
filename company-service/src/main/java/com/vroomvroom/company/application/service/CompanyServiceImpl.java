package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.validator.CompanyValidator;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.entity.Company;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.domain.vo.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyValidator companyValidator;
    private final CompanyRepository companyRepository;

    @Transactional
    public UUID createCompany(CreateCompanyCommand command) {
        log.info("업체 생성 시작");

        CompanyType companyType = parseCompanyType(command.companyType());

        companyValidator.validate(command);

        Company company = Company.create(command.hubId(), command.companyManagerId(), command.companyName(), command.companyAddress(), companyType);

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
}
