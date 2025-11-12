//package com.vroomvroom.hub.infrastructure.external.client;
//
//import com.vroomvroom.common.api.ApiResponse;
//import com.vroomvroom.common.exception.CustomException;
//import com.vroomvroom.hub.application.command.CreateStockCommand;
//import com.vroomvroom.hub.application.service.ProductClient;
//import com.vroomvroom.hub.domain.vo.ProductId;
//import com.vroomvroom.hub.exception.HubErrorCode;
//import com.vroomvroom.hub.infrastructure.external.dto.ProductDTO;
//import feign.FeignException;
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
//        try {
//            return productFeignClient.exists(productId);
//        } catch (FeignException e) {
//            throw new CustomException(HubErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
//        }
//    }
//
//    @Override
//    public ProductDTO getProduct(UUID productId) {
//        try {
//            ApiResponse<ProductDTO> res = productFeignClient.getProduct(productId);
//            if (!res.isSuccess() || res.getData() == null) throw new CustomException(HubErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
//            return res.getData();
//        } catch (FeignException.NotFound e) {
//            throw new CustomException(HubErrorCode.PRODUCT_NOT_FOUND);
//        } catch (FeignException e) {
//            throw new CustomException(HubErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
//        }
//    }
//}