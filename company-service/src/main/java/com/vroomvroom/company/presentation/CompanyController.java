package com.vroomvroom.company.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/companies")
public class CompanyController {

    @PostMapping
    public void createCompany() {
        // 업체 생성
    }
}
