package com.vroomvroom.company.application.port;

import com.vroomvroom.company.domain.vo.CompanyManagerId;

public interface UserClient {
    boolean existsUser(CompanyManagerId companyManagerId);
}
