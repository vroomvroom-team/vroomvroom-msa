package com.vroomvroom.company.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),
    NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "해당 리소스 소유자가 아닙니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // Valid
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 데이터의 형식이 유효하지 않습니다."),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다."),
    INVALID_COMPANY_TYPE(HttpStatus.BAD_REQUEST, "알맞지 않은 업체 타입입니다."),

    // company
    COMPANY_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "업체 이름은 필수입니다."),
    COMPANY_ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "업체 주소는 필수입니다."),
    COMPANY_MANAGER_REQUIRED(HttpStatus.BAD_REQUEST, "업체 관리자 ID는 필수입니다."),
    HUB_ID_REQUIRED(HttpStatus.BAD_REQUEST, "허브 ID는 필수입니다."),
    COMPANY_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "업체 타입은 필수입니다."),
    DUPLICATE_COMPANY_NAME(HttpStatus.CONFLICT, "이미 존재하는 업체 이름입니다."),
    DUPLICATE_COMPANY_ADDRESS(HttpStatus.CONFLICT, "이미 존재하는 업체 주소입니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업체 ID 입니다."),
    COMPANY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 업체입니다."),

    // product
    DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "이미 존재하는 상품 이름입니다."),
    HUB_MISMATCH(HttpStatus.BAD_REQUEST, "허브 정보가 일치하지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 0보다 큰 값이어야 합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 ID 입니다."),


    // 외부 서비스 검증 실패
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 허브 ID입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 ID입니다."),
    HUB_LOOKUP_FAILED(HttpStatus.BAD_GATEWAY, "허브 조회에 실패했습니다."),
    HUB_MANAGER_LOOKUP_FAILED(HttpStatus.BAD_REQUEST, "허브 매니저 검증에 실패했습니다." )

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
