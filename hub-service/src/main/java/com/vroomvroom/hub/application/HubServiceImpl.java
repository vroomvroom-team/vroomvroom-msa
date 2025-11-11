package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.application.command.CreateStockCommand;
import com.vroomvroom.hub.application.command.DecreaseStockCommand;
import com.vroomvroom.hub.application.command.UpdateHubCommand;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.application.dto.StockRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.entity.Stock;
//import com.vroomvroom.hub.domain.port.ProductClient;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.repository.HubRouteRepository;
import com.vroomvroom.hub.domain.vo.ProductId;
import com.vroomvroom.hub.exception.CustomException;
import com.vroomvroom.hub.exception.ErrorCode;
import com.vroomvroom.hub.infrastructure.kafka.StockDecreasedEvent;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.presentation.dto.response.CreateStockRes;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
//    private final ProductClient productClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public CreateHubRes createHub(CreateHubCommand command) {
        if (hubRepository.existsByHubName(command.getHubName())) throw new CustomException(ErrorCode.DUPLICATE_HUB_NAME);
        Hub hub = Hub.of(
                command.getHubName(),
                command.getAddress(),
                command.getLatitude(),
                command.getLongitude()
        );
        return CreateHubRes.from(hubRepository.save(hub));
    }

    @Override
    public PageResponse<HubListRes> getHubList(Pageable pageable) {
        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNull(pageable);
        return PageResponse.fromPage(hubs.map(HubListRes::from));
    }

    @Override
    @Cacheable(cacheNames = "hubCache", key = "#hubId")
    public HubDetailRes getHubDetail(UUID hubId) {
        Hub hub = findHubById(hubId);
        return HubDetailRes.from(hub);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"hubCache", "hubRouteCache", "optimalRouteCache"}, allEntries = true)
    public void updateHub(UUID hubId, UpdateHubCommand command) {
        Hub hub = findHubById(hubId);
        hub.update(command.getHubName(), command.getAddress(), command.getLatitude(), command.getLongitude());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"hubCache", "hubRouteCache", "optimalRouteCache"}, allEntries = true)
    public void deleteHub(UUID hubId) {
        Hub hub = findHubById(hubId);
        hub.delete();
        List<HubRoute> routes = hubRouteRepository.findAllByHubId(hubId);
        routes.forEach(HubRoute::markAsDeleted);
    }

    @Override
    public boolean existsHub(UUID hubId) {
        return hubRepository.existsById(hubId);
    }

    @Override
    @Transactional
    public CreateStockRes createStock(CreateStockCommand command) {
//        try {
//            boolean exists = productClient.exists(command.getProductId());
//            if (!exists) throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
//        } catch (FeignException e) {
//            throw new CustomException(ErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
//        }
        Hub hub = findHubById(command.getHubId());
        ProductId productId = ProductId.of(command.getProductId());
        Stock stock = hub.getStocks().stream()
                .filter(s -> s.getProductId().equals(productId) && s.getDeletedAt() == null)
                .findFirst()
                .orElse(null);
        if (stock != null) stock.increase(command.getQuantity());
        else {
            stock = Stock.of(productId, hub, command.getQuantity());
            hub.createStock(stock);
        }
        return CreateStockRes.from(stock);
    }

    @Override
    public List<StockRes> getStockList(UUID hubId) {
        Hub hub = findHubById(hubId);
        return StockRes.fromList(hub.getStocks());
    }

    @Override
    @Transactional
    public void decreaseStock(DecreaseStockCommand command) {
        Hub hub = findHubById(command.getHubId());
        Stock stock = findStockById(command.getHubId(), command.getProductId());
        stock.decrease(command.getQuantity());
        try {
            StockDecreasedEvent event = new StockDecreasedEvent(
                    command.getOrderId(),
                    command.getHubId(),
                    command.getProductId(),
                    command.getQuantity(),
                    System.currentTimeMillis()
            );
            kafkaTemplate.send("stock-decreased", event);
        } catch (Exception e) {
            log.error("재고 감소 이벤트 발행 실패, 주문 아이디: {}", command.getOrderId(), e);
        }
    }

    @Override
    public StockRes getStock(UUID hubId, UUID productId) {
        Stock stock = findStockById(hubId, productId);
        return StockRes.from(stock);
    }

    Hub findHubById(UUID hubId) {
        return hubRepository.findHubByHubIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_NOT_FOUND));
    }

    Stock findStockById(UUID hubId, UUID productId) {
        Hub hub = findHubById(hubId);
        return hub.getStocks().stream()
                .filter(s -> s.getProductId().equals(ProductId.of(productId)) && s.getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.STOCK_NOT_FOUND));
    }
}