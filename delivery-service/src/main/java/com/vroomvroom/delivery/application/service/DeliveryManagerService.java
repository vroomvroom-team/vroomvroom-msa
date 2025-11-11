package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerService {

    CreateManagerRes createManager(CreateManagerCommand request);

    Page<DeliveryManagerRes> getDeliveryManagers(DeliveryManagerType type, Pageable pageable);

    DeliveryManagerRes getDelivery(Long id);

    DeliveryManagerRes deleteDelivery(Long id);

    /**
     * 생성된 배송의 첫번째 경로에 대해 담당자 배정 이벤트 발행
     */
    void assignManagerToDelivery(Delivery delivery);
}
