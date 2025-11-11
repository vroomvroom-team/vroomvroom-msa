package com.vroomvroom.company.application.validator;

import com.vroomvroom.company.application.port.HubClient;
import com.vroomvroom.company.common.enums.UserRole;
import com.vroomvroom.company.common.exception.CustomException;
import com.vroomvroom.company.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorityValidator {

    private final HubClient hubClient;

    public void validateCreateCompanyAuthority(UUID hubId, Long userId, UserRole userRole) {
        validateMasterOrHubManager(hubId, userId, userRole);
    }

    public void validateCreateProductAuthority(UUID hubId, Long companyManagerId, Long userId, UserRole role) {
        if (role == UserRole.COMPANY_MANAGER) {
            if (!Objects.equals(companyManagerId, userId)) throw new CustomException(ErrorCode.FORBIDDEN);
            return;
        }

        validateMasterOrHubManager(hubId, userId, role);
    }

    public void validateUpdateAuthority(UUID hubId, Long companyManagerId, Long userId, UserRole role) {
        if (role == UserRole.COMPANY_MANAGER) {
            if (!Objects.equals(companyManagerId, userId)) throw new CustomException(ErrorCode.FORBIDDEN);
            return;
        }

        validateMasterOrHubManager(hubId, userId, role);
    }

    public void validateDeleteAuthority(UUID hubId, Long userId, UserRole userRole) {
        validateMasterOrHubManager(hubId, userId, userRole);
    }

    private void validateMasterOrHubManager(UUID hubId, Long userId, UserRole role) {
        if (role == UserRole.MASTER) return;

        if (role == UserRole.HUB_MANAGER) {
            boolean exists = hubClient.existsHubManager(hubId, userId);
            if (!exists) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }

            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }
}
