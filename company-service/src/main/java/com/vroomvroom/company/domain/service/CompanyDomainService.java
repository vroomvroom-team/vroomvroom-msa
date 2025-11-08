package com.vroomvroom.company.domain.service;

import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import com.vroomvroom.company.domain.vo.CompanyAddress;
import com.vroomvroom.company.domain.vo.CompanyName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyDomainService {

    private final CompanyRepository companyRepository;

    public void validateDuplicateCompanyName(CompanyName companyName) {
        if (companyRepository.existsByCompanyName(companyName))
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_NAME);
    }

    public void validateDuplicateCompanyAddress(CompanyAddress companyAddress) {
        if (companyRepository.existsByCompanyAddress(companyAddress))
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_ADDRESS);
    }
}
