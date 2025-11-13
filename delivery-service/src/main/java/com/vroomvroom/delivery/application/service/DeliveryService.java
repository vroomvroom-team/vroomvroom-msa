package com.vroomvroom.delivery.application.service;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.delivery.application.command.CompleteDeliveryCommand;
import com.vroomvroom.delivery.application.command.CreateDeliveryCommand;
import com.vroomvroom.delivery.presentation.dto.response.CreateDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetAllDeliveryRes;
import com.vroomvroom.delivery.presentation.dto.response.GetDeliveryRes;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface DeliveryService {

    /**
     * manager는 이미 존재. 여기서는 루트에 매니저 배정해주는 메서드만 호출하면 됨.
     */
    CreateDeliveryRes createDelivery(CreateDeliveryCommand command);

    /**
     * 배송 상태 변경 (허브 -> 업체)
     */
    void handoffToCompany(UUID deliveryId);

    void completeDelivery(UUID deliveryId, CompleteDeliveryCommand command);

    PageResponse<GetAllDeliveryRes> getAllDelivery(Pageable pageable);

    GetDeliveryRes getDelivery(UUID deliveryId);

    void cancelDelivery(UUID deliveryId);
}
