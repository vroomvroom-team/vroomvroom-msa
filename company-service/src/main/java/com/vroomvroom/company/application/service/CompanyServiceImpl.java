package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.command.UpdateCompanyCommand;
import com.vroomvroom.company.application.dto.CompanyResult;
import com.vroomvroom.company.application.validator.CompanyAuthorityValidator;
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
    private final CompanyAuthorityValidator companyAuthorityValidator;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResult createCompany(CreateCompanyCommand command) {
        log.info("업체 생성 시작");

/*        TODO. 유저 권한 체크(MASTER, HUB_MANAGER)
        companyAuthorityValidator.validateCreatePermission(
                command.hubId(),
                command.userId(),
                command.userRole()
        );*/

        CompanyType companyType = parseCompanyType(command.companyType());

        companyValidator.validateForCreate(command);

        Company company = Company.create(
                command.hubId(),
                command.companyManagerId(),
                command.companyName(),
                command.companyAddress(),
                companyType
        );

        Company savedCompany = companyRepository.save(company);

        log.info("업체 생성 완료: companyId = {}", savedCompany.getCompanyId());
        return CompanyResult.form(savedCompany);
    }

    private CompanyType parseCompanyType(String type) {
        try {
            return CompanyType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_COMPANY_TYPE);
        }
    }

    @Override
    public CompanyResult getCompany(UUID companyId) {
        log.info("업체 상세 조회 시작");

        Company company = companyRepository.findByCompanyId(companyId)
                        .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        company.validateNotDeleted();

        log.info("업체 상세 조회 완료: companyId = {}", company.getCompanyId());
        return CompanyResult.form(company);
    }

    @Override
    @Transactional
    public CompanyResult updateCompany(UpdateCompanyCommand command) {
        Company company = companyRepository.findByCompanyId(command.companyId())
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));

        company.validateNotDeleted();

/*        TODO. 유저 권한 체크(MASTER, HUB_MANAGER) <- 아닐 경우 requesterId 검사 추가 (로그인한 사용자가 companyManagerId와 같은지)
        companyAuthorityValidator.validateUpdatePermission(
                company.getHubId(),
                company.getCompanyManagerId(),
                command.userId(),
                command.userRole()
        );*/

        CompanyUpdates(company, command);

        log.info("업체 수정 완료: companyId = {}", company.getCompanyId());
        return CompanyResult.form(company);
    }

    private void CompanyUpdates(Company company, UpdateCompanyCommand command) {
        if (company.getHubId() != null) {
            companyValidator.validateHub(company.getHubId());
            company.changeHub(company.getHubId());
        }

        if (command.companyManagerId() != null) {
            companyValidator.validateManager(command.companyManagerId());
            company.changeManager(command.companyManagerId());
        }

        if (command.companyName() != null) {
            companyValidator.validateDuplicateName(command.companyName());
            company.changeName(command.companyName());
        }

        if (command.companyAddress() != null) {
            companyValidator.validateDuplicateAddress(command.companyAddress());
            company.changeAddress(command.companyAddress());
        }

        if (command.companyType() != null) {
            company.changeType(parseCompanyType(command.companyType()));
        }
    }
}
