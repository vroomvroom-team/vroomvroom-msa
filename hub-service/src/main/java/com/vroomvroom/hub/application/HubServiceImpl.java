package com.vroomvroom.hub.application;

import com.vroomvroom.common.api.PageResponse;
import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.hub.application.command.*;
import com.vroomvroom.hub.application.dto.HubDetailRes;
import com.vroomvroom.hub.application.dto.HubListRes;
import com.vroomvroom.hub.application.dto.HubManagerRes;
import com.vroomvroom.hub.application.dto.StockRes;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.entity.HubRoute;
import com.vroomvroom.hub.domain.entity.Stock;
//import com.vroomvroom.hub.domain.port.ProductClient;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.domain.repository.HubRouteFindRepository;
import com.vroomvroom.hub.domain.vo.ProductId;
import com.vroomvroom.hub.exception.HubErrorCode;
import com.vroomvroom.hub.infrastructure.kafka.StockDecreasedEvent;
import com.vroomvroom.hub.infrastructure.kafka.StockIncreasedEvent;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import com.vroomvroom.hub.presentation.dto.response.CreateStockRes;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final HubRouteFindRepository hubRouteFindRepository;
//    private final ProductClient productClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public CreateHubRes createHub(CreateHubCommand command) {
        if (hubRepository.existsByHubName(command.getHubName())) throw new CustomException(HubErrorCode.DUPLICATE_HUB_NAME);
        Hub hub = Hub.of(
                command.getHubName(),
                command.getAddress(),
                command.getLatitude(),
                command.getLongitude(),
                command.getHubManagerId()
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
        hub.update(command.getHubName(), command.getAddress(), command.getLatitude(), command.getLongitude(), command.getHubManagerId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"hubCache", "hubRouteCache", "optimalRouteCache"}, allEntries = true)
    public void deleteHub(UUID hubId) {
        Hub hub = findHubById(hubId);
        hub.delete();
        List<HubRoute> arrivalRoutes = hub.getArrivalRoutes();
        List<HubRoute> departureRoutes = hub.getDepartureRoutes();
        arrivalRoutes.forEach(HubRoute::markAsDeleted);
        departureRoutes.forEach(HubRoute::markAsDeleted);
        List<Stock> stocks = hub.getStocks();
        stocks.forEach(Stock::markAsDeleted);
    }

    @Override
    public boolean existsHub(UUID hubId) {
        return hubRepository.existsById(hubId);
    }

    @Override
    public HubManagerRes getHubManager(UUID hubId) {
        Long managerId = hubRepository.findHubManagerIdByHubId(hubId)
                .orElseThrow(() -> new CustomException(HubErrorCode.MANAGER_NOT_FOUND));
        return new HubManagerRes(managerId);
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
        return StockRes.fromList(hub.getStocks().stream().filter(
                s -> s.getDeletedAt() == null).collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public void decreaseStock(DecreaseStockCommand command) {
        Stock stock = findStockByProductId(command.getHubId(), command.getProductId());
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
    @Transactional
    public void increaseStock(IncreaseStockCommand command) {
        Stock stock = findStockByProductId(command.getHubId(), command.getProductId());
        stock.increase(command.getQuantity());
        try {
            StockIncreasedEvent event = new StockIncreasedEvent(
                    command.getOrderId(),
                    command.getHubId(),
                    command.getProductId(),
                    command.getQuantity(),
                    System.currentTimeMillis()
            );
            kafkaTemplate.send("stock-increased", event);
        } catch (Exception e) {
            log.error("재고 증가 이벤트 발행 실패, 주문 아이디: {}", command.getOrderId(), e);
        }
    }

    @Override
    public StockRes getStock(UUID hubId, UUID productId) {
        Stock stock = findStockByProductId(hubId, productId);
        return StockRes.from(stock);
    }

    @Override
    @Transactional
    public void deleteStock(UUID hubId, UUID stockId) {
        Stock stock = findStockById(hubId, stockId);
        stock.markAsDeleted();
    }

    Hub findHubById(UUID hubId) {
        return hubRepository.findHubByHubIdAndDeletedAtIsNull(hubId)
                .orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
    }

    Stock findStockByProductId(UUID hubId, UUID productId) {
        Hub hub = findHubById(hubId);
        return hub.getStocks().stream()
                .filter(s -> s.getProductId().equals(ProductId.of(productId)) && s.getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new CustomException(HubErrorCode.STOCK_NOT_FOUND));
    }

    Stock findStockById(UUID hubId, UUID stockId) {
        Hub hub = findHubById(hubId);
        return hub.getStocks().stream()
                .filter(s -> s.getStockId().equals(stockId) && s.getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new CustomException(HubErrorCode.STOCK_NOT_FOUND));
    }
}