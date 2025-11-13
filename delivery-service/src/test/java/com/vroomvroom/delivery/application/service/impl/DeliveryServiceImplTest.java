package com.vroomvroom.delivery.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vroomvroom.common.exception.CustomException;
import com.vroomvroom.delivery.domain.entity.Delivery;
import com.vroomvroom.delivery.domain.exception.DeliveryErrorCode;
import com.vroomvroom.delivery.domain.repository.DeliveryRepository;
import com.vroomvroom.delivery.domain.vo.DeliveryStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @InjectMocks
    DeliveryServiceImpl deliveryServiceMock;

    @Mock
    Delivery deliveryMock;

    @Mock
    DeliveryRepository deliveryRepositoryMock;

    private final UUID TEST_DELIVERY_ID = UUID.randomUUID();

    @Nested
    @DisplayName("배송 취소 기능 테스트")
    class CancelFunctionTests {

        @Test
        @DisplayName("성공 : HUB_WAITING 상태의 배송을 취소한다.")
        void cancelDelivery_success() {
            when(deliveryRepositoryMock.findById(TEST_DELIVERY_ID))
                .thenReturn(Optional.of(deliveryMock));
            when(deliveryMock.getStatus()).thenReturn(DeliveryStatus.HUB_WAITING);

            deliveryServiceMock.cancelDelivery(TEST_DELIVERY_ID);

            verify(deliveryRepositoryMock, times(1)).findById(TEST_DELIVERY_ID);
            verify(deliveryMock, times(1)).updateStatus(DeliveryStatus.CANCELED);
        }

        @Test
        @DisplayName("실패: HUB_WAITING 상태가 아닌 배송은 취소 불가 예외를 던진다.")
        void cancelDelivery_fail_wrongStatus() {
            when(deliveryRepositoryMock.findById(TEST_DELIVERY_ID))
                .thenReturn(Optional.of(deliveryMock));
            when(deliveryMock.getStatus()).thenReturn(DeliveryStatus.DELIVERY_IN_PROGRESS);

            CustomException thrown = assertThrows(CustomException.class, () -> {
                deliveryServiceMock.cancelDelivery(TEST_DELIVERY_ID);
            });

            assertEquals(DeliveryErrorCode.DELIVERY_NOT_CANCEL, thrown.getErrorCode());

            verify(deliveryMock, never()).updateStatus(DeliveryStatus.CANCELED);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID로 호출시 NOT_FOUND 예외를 던진다.")
        void cancelDelivery_fail_notFound() {
            when(deliveryRepositoryMock.findById(TEST_DELIVERY_ID))
                .thenReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () -> {
                deliveryServiceMock.cancelDelivery(TEST_DELIVERY_ID);
            });

            assertEquals(DeliveryErrorCode.DELIVERY_NOT_FOUND, thrown.getErrorCode());
        }
    }

}