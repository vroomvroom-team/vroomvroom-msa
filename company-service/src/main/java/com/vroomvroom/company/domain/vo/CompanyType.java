package com.vroomvroom.company.domain.vo;

public enum CompanyType {
    SUPPLY("공급 업체"),
    RECEIPT("수령 업체");

    private final String description;

    CompanyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
