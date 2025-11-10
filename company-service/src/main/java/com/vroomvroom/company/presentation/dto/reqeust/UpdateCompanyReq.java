package com.vroomvroom.company.presentation.dto.reqeust;

import java.util.UUID;

public record UpdateCompanyReq (
        UUID hubId,
        Long companyManagerId,
        String companyName,
        String companyAddress,
        String companyType
) {}