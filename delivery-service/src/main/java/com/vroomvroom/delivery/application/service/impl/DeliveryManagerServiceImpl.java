package com.vroomvroom.delivery.application.service.impl;

import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.application.service.DeliveryManagerService;
import com.vroomvroom.delivery.domain.entity.DeliveryManager;
import com.vroomvroom.delivery.domain.exception.CustomException;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.port.HubClient;
import com.vroomvroom.delivery.domain.port.UserClient;
import com.vroomvroom.delivery.domain.repository.DeliveryManagerRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerSequence;
import com.vroomvroom.delivery.domain.vo.DeliveryManagerType;
import com.vroomvroom.delivery.domain.vo.HubId;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;
import com.vroomvroom.delivery.presentation.dto.response.DeliveryManagerRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryManagerServiceImpl implements DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;

    private final HubClient hubClient;
    private final UserClient userClient;

    @Override
    @Transactional
    public CreateManagerRes createManager(
            CreateManagerCommand request
    ) {
        if (deliveryManagerRepository.existsByDeliveryManagerId(request.getUserId())) {
            throw new CustomException(DeliveryErrorCode.MANAGER_ALREADY_EXISTS);
        }

        DeliveryManagerType managerType = DeliveryManagerType.valueOf(request.getType());
        return switch (managerType) {
            case HUB_MANAGER -> createHubManager(request);
            case COMPANY_MANAGER -> createCompanyManager(request);
        };
    }

    @Override
    public Page<DeliveryManagerRes> getDeliveryManagers(DeliveryManagerType type, Pageable pageable) {
        if (type == null)
            return deliveryManagerRepository.findAllDelivery(pageable).map(DeliveryManagerRes::from);

        return deliveryManagerRepository.findAllByType(type, pageable).map(DeliveryManagerRes::from);
    }

    @Override
    public DeliveryManagerRes getDelivery(Long id) {
        return deliveryManagerRepository.findDeliveryById(id).map(DeliveryManagerRes::from)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
    }

    @Override
    @Transactional
    public DeliveryManagerRes deleteDelivery(Long id) {
        DeliveryManager deliveryManager = deliveryManagerRepository.findDeliveryById(id)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        // 현재 할당된 배송이 있는지 확인 필요?

        deliveryManager.markAsDeleted();

        return DeliveryManagerRes.from(deliveryManager);
    }

    private CreateManagerRes createHubManager(CreateManagerCommand request) {
        userClient.verifyUserHasRole(request.getUserId(), DeliveryManagerType.HUB_MANAGER);

        Long sequence = deliveryManagerRepository.nextGlobalSequence();
        if (sequence == null) {
            throw new CustomException(DeliveryErrorCode.HUB_MANAGER_CAPACITY_FULL);
        }

        DeliveryManager hubManager = DeliveryManager.createHubManager(
                request.getUserId(),
                DeliveryManagerType.HUB_MANAGER,
                DeliveryManagerSequence.of(sequence)
        );

        try {
            return CreateManagerRes.from(deliveryManagerRepository.save(hubManager));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(DeliveryErrorCode.HUB_MANAGER_CAPACITY_FULL);
        }
    }

    private CreateManagerRes createCompanyManager(CreateManagerCommand request) {
        userClient.verifyUserHasRole(request.getUserId(), DeliveryManagerType.COMPANY_MANAGER);
        hubClient.verifyExists(request.getHubId());

        Long sequence = deliveryManagerRepository.nextHubSequence(request.getHubId());
        if (sequence == null) {
            throw new CustomException(DeliveryErrorCode.COMPANY_MANAGER_CAPACITY_FULL);
        }

        DeliveryManager companyManager = DeliveryManager.createCompanyManager(
                request.getUserId(),
                DeliveryManagerType.COMPANY_MANAGER,
                HubId.of(request.getHubId()),
                DeliveryManagerSequence.of(sequence)
        );

        try {
            return CreateManagerRes.from(deliveryManagerRepository.save(companyManager));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(DeliveryErrorCode.COMPANY_MANAGER_CAPACITY_FULL);
        }
    }
}
