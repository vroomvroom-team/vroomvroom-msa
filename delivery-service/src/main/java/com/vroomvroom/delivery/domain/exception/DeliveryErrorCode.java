package com.vroomvroom.delivery.domain.exception;

import com.vroomvroom.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {

    // DeliveryManager
    INVALID_MANAGER_SEQUENCE(HttpStatus.BAD_REQUEST, "잘못된 배송담당자 순번입니다."),
    INVALID_MANAGER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 담당자 타입입니다."),
    MANAGER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 배송매니저입니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 허브 ID입니다."),
    DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송 담당자 ID입니다."),
    HUB_MANAGER_CAPACITY_FULL(HttpStatus.CONFLICT, "허브 매니저 정원이 초과되었습니다."),
    COMPANY_MANAGER_CAPACITY_FULL(HttpStatus.CONFLICT, "해당 허브의 업체매니저 정원이 초과되었습니다."),
    MANAGER_NOT_AVAILABLE(HttpStatus.NOT_FOUND, "배송 가능한 담당자가 없습니다."),

    // Delivery
    START_HUB_EQUALS_ARRIVE_HUB(HttpStatus.BAD_REQUEST, "출발 허브와 도착 허브는 같을 수 없습니다."),
    ORDER_STATE_CONFLICT(HttpStatus.CONFLICT, "배송 불가능한 주문입니다."),
    ORDER_NOT_FOUND(HttpStatus.CONFLICT, "존재하지 않는 주문입니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송입니다."),
    DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송경로입니다."),
    HUB_MOVE_WAITING(HttpStatus.BAD_REQUEST, "허브대기중인 배송만 담당자 배정이 가능합니다."),
    HUB_MOVING(HttpStatus.BAD_REQUEST, "허브이동중인 배송만 배송완료로 상태변경 가능합니다."),
    DELIVERY_IN_PROGRESS_REQUIRED(HttpStatus.BAD_REQUEST, "배송중 상태만 요구되는 작업입니다."),
    ROUTE_NOT_ARRIVED_YET(HttpStatus.CONFLICT, "아직 허브배송이 완료되지 않았습니다."),
    DELIVERY_STATE_CONFLICT(HttpStatus.CONFLICT, "잘못된 배송 상태입니다."),
    DELIVERY_NOT_CANCEL(HttpStatus.CONFLICT, "현재는 배송 취소가 불가능한 상태입니다,"),
    DELIVERY_ID_MISMATCH(HttpStatus.BAD_REQUEST, "해당 배송경로가 해당 배송에 속하지 않습니다."),
    MANAGER_ALREADY_ACTIVE(HttpStatus.CONFLICT, "해당 담당자는 이미 배송중입니다."),
    ASSIGNMENT_DATA_INCONSISTENCY(HttpStatus.INTERNAL_SERVER_ERROR, "Redis와 DB의 데이터가 일치하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
