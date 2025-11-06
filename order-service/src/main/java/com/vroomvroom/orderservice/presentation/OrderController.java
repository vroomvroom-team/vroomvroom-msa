package com.vroomvroom.orderservice.presentation;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class OrderController {

    @GetMapping("/")
    public String test() {
        return "Order Service";
    }
}
