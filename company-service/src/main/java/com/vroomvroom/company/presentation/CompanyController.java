package com.vroomvroom.company.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.service.CompanyServiceImpl;
import com.vroomvroom.company.presentation.dto.reqeust.CreateCompanyReq;
import com.vroomvroom.company.presentation.dto.response.CompanyCreateRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private final CompanyServiceImpl companyServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyCreateRes>> createCompany(@RequestBody @Valid CreateCompanyReq req) {
        log.info("POST api/v1/companies 업체 생성 요청");

        CreateCompanyCommand command = new CreateCompanyCommand(
                req.hubId(),
                req.companyManagerId(),
                req.companyName(),
                req.companyAddress(),
                req.companyType()
        );

        UUID companyId = companyServiceImpl.createCompany(command);

        CompanyCreateRes response = new CompanyCreateRes(companyId, "업체가 성공적으로 생성되었습니다.");

        log.info("업체 생성 성공: companyId = {}", companyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
