package com.vroomvroom.company.infrastructure.external.adapter;

import com.vroomvroom.company.application.port.UserClient;
import com.vroomvroom.company.infrastructure.external.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
}
