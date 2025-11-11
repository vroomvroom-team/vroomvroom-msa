package com.vroomvroom.company.application.service;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.command.DeleteCompanyCommand;
import com.vroomvroom.company.application.command.UpdateCompanyCommand;
import com.vroomvroom.company.application.dto.CompanyResult;
import com.vroomvroom.company.application.validator.AuthorityValidator;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyValidator companyValidator;
    private final AuthorityValidator authorityValidator;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResult createCompany(CreateCompanyCommand command) {
        log.info("업체 생성 시작");

/*        TODO. 유저 권한 체크
        authorityValidator.validateCreateCompanyAuthority(
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
        return CompanyResult.from(savedCompany);
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

        Company company = getActiveCompany(companyId);

        log.info("업체 상세 조회 완료: companyId = {}", company.getCompanyId());
        return CompanyResult.from(company);
    }

    @Override
    @Transactional
    public CompanyResult updateCompany(UpdateCompanyCommand command) {
        Company company = getActiveCompany(command.companyId());

/*        TODO. 유저 권한 체크
        authorityValidator.validateUpdateAuthority(
                company.getHubId(),
                company.getCompanyManagerId(),
                command.userId(),
                command.userRole()
        );*/

        companyUpdates(company, command);

        log.info("업체 수정 완료: companyId = {}", company.getCompanyId());
        return CompanyResult.from(company);
    }

    private void companyUpdates(Company company, UpdateCompanyCommand command) {
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

    @Override
    @Transactional
    public UUID deleteCompany(DeleteCompanyCommand command) {
        Company company = getActiveCompany(command.companyId());

/*        TODO. 유저 권한 체크
        authorityValidator.validateDeleteAuthority(
                company.getHubId(),
                command.userId(),
                command.userRole()
        );*/

        company.markAsDeleted();
        return company.getCompanyId();
    }

    private Company getActiveCompany(UUID companyId) {
        return companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
    }
}
