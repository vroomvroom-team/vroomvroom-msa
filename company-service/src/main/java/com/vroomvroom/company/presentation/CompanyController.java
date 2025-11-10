package com.vroomvroom.company.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.service.CompanyService;
import com.vroomvroom.company.presentation.dto.reqeust.CreateCompanyReq;
import com.vroomvroom.company.presentation.dto.response.CompanyCreateRes;
import com.vroomvroom.company.presentation.dto.response.CompanyDetailRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private final CompanyService companyService;

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

        UUID companyId = companyService.createCompany(command);

        CompanyCreateRes response = new CompanyCreateRes(companyId, "업체가 성공적으로 생성되었습니다.");

        log.info("업체 생성 성공: companyId = {}", companyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyDetailRes>> getCompany(@PathVariable UUID companyId) {
        log.info("GET api/v1/companies/{} 업체 상세조회 요청", companyId);

        CompanyDetailRes response = CompanyDetailRes.form(companyService.getCompany(companyId));

        log.info("업체 상세 조회 성공: companyId = {}", companyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
