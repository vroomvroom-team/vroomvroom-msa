package com.vroomvroom.orderservice.exception;

import com.vroomvroom.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "주문 서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),
    NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "해당 리소스 소유자가 아닙니다."),

    // Validation
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 데이터의 형식이 유효하지 않습니다."),

    // 주문 관련 에러
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    ORDER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다."),
    ORDER_NOT_CANCELLABLE(HttpStatus.BAD_REQUEST, "취소 가능한 상태의 주문이 아닙니다."),
    ORDER_NOT_MODIFIABLE(HttpStatus.BAD_REQUEST, "수정 가능한 상태의 주문이 아닙니다."),
    ORDER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "주문 생성에 실패했습니다."),
    ORDER_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "주문 수정에 실패했습니다."),

    // 외부 서비스 - 업체 서비스
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업체입니다."),
    COMPANY_HUB_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "업체의 허브 정보를 찾을 수 없습니다."),
    COMPANY_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "업체 서비스에 연결할 수 없습니다."),
    COMPANY_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "업체 서버 내부 오류가 발생했습니다."),

    // 외부 서비스 - 상품 서비스
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
    PRODUCT_STOCK_DECREASE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "상품 재고 감소에 실패했습니다."),
    PRODUCT_STOCK_INCREASE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "상품 재고 증가에 실패했습니다."),
    PRODUCT_STOCK_ROLLBACK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "상품 재고 원복에 실패했습니다."),
    PRODUCT_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "상품 서비스에 연결할 수 없습니다."),
    PRODUCT_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "상품 서버 내부 오류가 발생했습니다."),

    // 외부 서비스 - 배송 서비스
    DELIVERY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배송 생성에 실패했습니다."),
    DELIVERY_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "배송 서비스에 연결할 수 없습니다."),
    DELIVERY_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "배송 서버 내부 오류가 발생했습니다."),

    // 비즈니스 로직 에러
    INVALID_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "주문 수량은 1개 이상이어야 합니다."),
    INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "납기일은 현재 시간 이후여야 합니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "주문 금액이 올바르지 않습니다."),
    SAME_SUPPLY_RECEIVE_COMPANY(HttpStatus.BAD_REQUEST, "공급 업체와 수령 업체가 동일할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
