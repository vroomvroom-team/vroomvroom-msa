package com.vroomvroom.company.infrastructure.external.adapter;

import com.vroomvroom.company.application.port.UserClient;
import com.vroomvroom.company.infrastructure.external.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserClientImpl implements UserClient {

    private final UserFeignClient userFeignClient;

    @Override
    public boolean existsUser(Long companyManagerId) {
        log.info("유저 존재 여부 확인 요청: companyManagerId = {}", companyManagerId);
        
/*        try {
            return userFeignClient.existsUser(companyManagerId.getId());
        } catch (Exception e) {
            log.error("[UserClient] 유저 조회 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.USER_LOOKUP_FAILED);
        }*/

        // TODO. 임시 구현
        return true;
    }

    @Override
    public Optional<UUID> getHubIdByUserId(Long userId) {
        log.info("유저 허브 ID 요청: userId = {}", userId);

/*        try {
            return userFeignClient.getHubIdByUserId(userId);
        } catch (Exception e) {
            log.error("[UserClient] 유저 허브 ID 요청 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.USER_LOOKUP_FAILED);
        }*/

        // TODO. 임시 구현
        UUID fakeHubId = UUID.fromString("b1a23d4f-56e7-8901-2345-6789abcdef01");
        return Optional.of(fakeHubId);
    }
}
