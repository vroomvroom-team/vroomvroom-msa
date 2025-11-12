//package com.vroomvroom.hub.infrastructure.external.client;
//
//import com.vroomvroom.hub.domain.port.ProductClient;
//import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class ProductClientImpl implements ProductClient {
//
//    private final ProductFeignClient productFeignClient;
//
//    @Override
//    public boolean exists(UUID productId) {
//        return productFeignClient.exists(productId);
//    }
//
//    @Override
//    public ProductDTO getProduct(UUID productId) {
//        return productFeignClient.getProduct(productId);
//    }
//}