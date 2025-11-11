package com.vroomvroom.company.presentation;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.company.application.command.CreateCompanyCommand;
import com.vroomvroom.company.application.command.DeleteCompanyCommand;
import com.vroomvroom.company.application.command.UpdateCompanyCommand;
import com.vroomvroom.company.application.service.CompanyService;
import com.vroomvroom.company.presentation.dto.request.CreateCompanyReq;
import com.vroomvroom.company.presentation.dto.request.UpdateCompanyReq;
import com.vroomvroom.company.presentation.dto.response.CompanyDeletedRes;
import com.vroomvroom.company.presentation.dto.response.CompanyDetailRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyDetailRes>> createCompany(
            @RequestBody @Valid CreateCompanyReq req
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/
    ) {
        log.info("POST api/v1/companies 업체 생성 요청");

        CreateCompanyCommand command = new CreateCompanyCommand(
                req.hubId(),
                req.companyManagerId(),
                req.companyName(),
                req.companyAddress(),
                req.companyType()
                // TODO. userId, userRole 추가
        );

        CompanyDetailRes response = CompanyDetailRes.from(companyService.createCompany(command));

        log.info("업체 생성 성공: companyId = {}", response.companyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyDetailRes>> getCompany(@PathVariable UUID companyId) {
        log.info("GET api/v1/companies/{} 업체 상세조회 요청", companyId);

        CompanyDetailRes response = CompanyDetailRes.from(companyService.getCompany(companyId));

        log.info("업체 상세 조회 성공: companyId = {}", response.companyId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyDetailRes>> updateCompany(
            @PathVariable UUID companyId,
            @RequestBody UpdateCompanyReq req
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/

    ) {
        log.info("PATCH api/v1/companies/{} 업체 수정 요청", companyId);

        UpdateCompanyCommand command = new UpdateCompanyCommand(
                companyId,
                req.companyManagerId(),
                req.companyName(),
                req.companyAddress(),
                req.companyType()
                // TODO. userId, userRole 추가
        );

        CompanyDetailRes response = CompanyDetailRes.from(companyService.updateCompany(command));

        log.info("업체 수정 성공: companyId = {}", response.companyId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyDeletedRes>> deleteCompany(
            @PathVariable UUID companyId
/*             TODO. 유저 정보 받아오기
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String userRole*/
    ) {
        log.info("DELETE api/v1/companies/{} 업체 삭제 요청", companyId);

        DeleteCompanyCommand command = new DeleteCompanyCommand(
                companyId
                // TODO. userId, userRole 추가
        );

        UUID DeletedCompanyId = companyService.deleteCompany(command); // TODO. 추후 command로 수정

        CompanyDeletedRes response = new CompanyDeletedRes(
                DeletedCompanyId,
                "업체가 성공적으로 삭제되었습니다."
        );

        log.info("업체 삭제 성공: companyId = {}", response.companyId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
