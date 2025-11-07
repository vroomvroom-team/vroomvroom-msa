package com.vroomvroom.orderservice.application;


import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderRes createOrder(CreateOrderCommand command);

    OrderRes getOrder(UUID orderId);

    Page<OrderRes> getOrders(Pageable pageable);
}
