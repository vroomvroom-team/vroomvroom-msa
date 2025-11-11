package com.vroomvroom.orderservice.infrastructure.external;

import com.vroomvroom.common.api.ApiResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.orderservice.application.service.HubClient;
import com.vroomvroom.orderservice.exception.OrderErrorCode;
import com.vroomvroom.orderservice.infrastructure.dto.StockDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

    private final HubFeignClient hubFeignClient;

    @Override
    public StockDTO getStockInfo(UUID hubId, UUID productId) {
        log.info("재고 조회 요청 : hubId={}", hubId);
        try {
            ApiResponse<StockDTO> response = hubFeignClient.getStockInfo(hubId, productId);

            if (!response.isSuccess() || response.getData() == null) {
                log.error("재고 조회 실패 : hubId = {}", hubId);
                throw new CustomException(OrderErrorCode.STOCK_NOT_FOUND);
            }

            log.info("재고 정보 조회 성공 : hubId = {}", hubId);
            return response.getData();
        } catch (FeignException.NotFound e) {
            log.error("재고를 찾을 수 없음 : hubId = {}", hubId);
            throw new CustomException(OrderErrorCode.STOCK_NOT_FOUND);
        } catch (FeignException e) {
            log.error("허브 서비스 호출 실패 : status={}, message={}", e.status(), e.getMessage());
            throw new CustomException(OrderErrorCode.HUB_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void decreaseStocks(UUID productId, long quantity) {
        log.info("재고 감소 요청 : productId={}, quantity={}", productId, quantity);

        /*
        try {
            ApiResponse<Void> response =
                    hubFeignClient.decreaseStock(productId, quantity);

            if (!response.isSuccess()) {
                log.error("재고 감소 실패: productId={}, quantity={}",
                        productId, quantity);
            }

        } catch (FeignException.BadRequest e) {
            log.error("재고 부족: productId={}, quantity={}", productId, quantity);
            throw new CustomException(OrderErrorCode.BAD_REQUEST);

        } catch (FeignException e) {
            log.error("재고 감소 API 호출 실패: status={}, message={}",
                    e.status(), e.getMessage());
            throw new CustomException(OrderErrorCode.INTERNAL_SERVER_ERROR);
        }
        */
    }

    @Override
    public void increaseStocks(UUID productId, long quantity) {
        log.info("재고 증가 요청 : productId={}, quantity={}", productId, quantity);

        /*
        try {
            hubFeignClient.increaseStock(productId, quantity);
            log.info("재고 증가 성공: productId={}, quantity={}", productId, quantity);

        } catch (FeignException e) {
            log.error("재고 증가 실패: productId={}, quantity={}, status={}, message={}",
                    productId, quantity, e.status(), e.getMessage());
        }
        */
    }
}
