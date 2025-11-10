package com.vroomvroom.company.application.validator;

import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.application.port.UserClient;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import com.vroomvroom.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyValidator {

    private final CompanyRepository companyRepository;
    private final HubClient hubClient;
    private final UserClient userClient;

    public void validateForCreate(CreateCompanyCommand command) {
        validateHub(command.hubId());
        validateManager(command.companyManagerId());
        validateDuplicateName(command.companyName());
        validateDuplicateAddress(command.companyAddress());
    }

    public void validateHub(UUID hubId) {
        if (!hubClient.existsHub(hubId)) {
            throw new CustomException(ErrorCode.HUB_NOT_FOUND);
        }
    }

    public void validateManager(Long companyManagerId) {
        if (!userClient.existsUser(companyManagerId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void validateDuplicateName(String name) {
        if (companyRepository.existsByCompanyName(name)) {
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_NAME);
        }
    }

    public void validateDuplicateAddress(String address) {
        if (companyRepository.existsByCompanyAddress(address)) {
            throw new CustomException(ErrorCode.DUPLICATE_COMPANY_ADDRESS);
        }
    }
}
