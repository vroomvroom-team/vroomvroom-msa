package com.vroomvroom.hub.application;

import com.vroomvroom.hub.application.command.CreateHubCommand;
import com.vroomvroom.hub.domain.entity.Hub;
import com.vroomvroom.hub.domain.repository.HubRepository;
import com.vroomvroom.hub.presentation.dto.response.CreateHubRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HubServiceImplTest {

    @Mock
    HubRepository hubRepository;

    @InjectMocks
    HubServiceImpl hubService;

    HubServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("허브 생성 테스트를 진행합니다.")
    void 새로운_허브_생성() {
        CreateHubCommand command = new CreateHubCommand(
                "서울특별시 센터",
                "서울특별시 송파구",
                BigDecimal.valueOf(37.2234),
                BigDecimal.valueOf(127.3423),
                1L
        );

        Hub mockHub = Hub.of(
                command.getHubName(),
                command.getAddress(),
                command.getLatitude(),
                command.getLongitude(),
                command.getHubManagerId()
        );

        when(hubRepository.existsByHubName(command.getHubName())).thenReturn(false);
        when(hubRepository.save(any(Hub.class))).thenReturn(mockHub);

        CreateHubRes res = hubService.createHub(command);

        assertThat(res.getHubName()).isEqualTo("서울특별시 센터");
        assertThat(res.getAddress()).isEqualTo("서울특별시 송파구");
        verify(hubRepository).save(any(Hub.class));
    }
}
