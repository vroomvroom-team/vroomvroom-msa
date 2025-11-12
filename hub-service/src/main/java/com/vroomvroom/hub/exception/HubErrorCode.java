package com.vroomvroom.hub.exception;

import com.vroomvroom.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HubErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 경로를 찾을 수 없습니다."),
    NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "해당 리소스 소유자가 아닙니다."),

    // 허브
    DUPLICATE_HUB_NAME(HttpStatus.CONFLICT, "해당 허브는 이미 존재하는 허브입니다."),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "유효하지 않은 위·경도입니다. (위도 범위: -90 ~ 90, 경도 범위: -180 ~ 180)"),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다."),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 관리자를 찾을 수 없습니다."),
    // 허브 경로
    SAME_DEPARTURE_ARRIVAL_HUB(HttpStatus.BAD_REQUEST, "출발 허브와 도착 허브가 같습니다."),
    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브 경로를 찾을 수 없습니다."),
    INVALID_TIME(HttpStatus.BAD_REQUEST, "유효하지 않은 소요 시간입니다."),
    INVALID_DISTANCE(HttpStatus.BAD_REQUEST, "유효하지 않은 이동 거리입니다."),
    DUPLICATE_HUB_ROUTE(HttpStatus.BAD_REQUEST, "중복되는 허브 경로가 존재합니다."),
    HUBS_NOT_CONNECTED(HttpStatus.BAD_REQUEST, "연결되어 있지 않은 허브는 경로 생성이 불가합니다."),
    NO_ACTIVE_ROUTE(HttpStatus.BAD_REQUEST, "활성화된 경로가 없습니다."),

    // 재고
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 업체를 찾을 수 없습니다."),
    DUPLICATE_STOCK(HttpStatus.CONFLICT, "이미 해당 상품의 재고가 존재합니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 재고를 찾을 수 없습니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 충분하지 않습니다."),

    PRODUCT_SERVICE_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "상품 서비스에 연결할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}