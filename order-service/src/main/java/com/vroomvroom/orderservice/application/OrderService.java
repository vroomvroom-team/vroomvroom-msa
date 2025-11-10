package com.vroomvroom.orderservice.application;


import com.vroomvroom.orderservice.application.command.CancelOrderCommand;
import com.vroomvroom.orderservice.application.command.CreateOrderCommand;
import com.vroomvroom.orderservice.application.command.UpdateOrderCommand;
import com.vroomvroom.orderservice.application.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderDTO createOrder(CreateOrderCommand command);

    OrderDTO getOrder(UUID orderId);

    Page<OrderDTO> getOrders(Pageable pageable);

    OrderDTO cancelOrder(CancelOrderCommand orderId);

    OrderDTO updateOrder(UpdateOrderCommand command);
}
