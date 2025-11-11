package com.vroomvroom.hub.infrastructure.external.client;

import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
//
//@FeignClient(name = "product-service", path = "/api/v1/products")
//public interface ProductFeignClient {
//
//    @GetMapping("/{productId}/exists")
//    boolean exists(@PathVariable UUID productId);
//
//    @GetMapping("/{productId}")
//    ProductDTO getProduct(@PathVariable("productId") UUID productId);
//}