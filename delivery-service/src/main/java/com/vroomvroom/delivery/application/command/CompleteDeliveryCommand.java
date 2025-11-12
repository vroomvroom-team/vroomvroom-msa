package com.vroomvroom.delivery.application.command;

import lombok.Getter;

@Getter
public class CompleteDeliveryCommand {

    private Long userId;
    private String userRole;

    private CompleteDeliveryCommand(Long userId, String userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }

    public static CompleteDeliveryCommand of(Long userId, String userRole) {
        return new CompleteDeliveryCommand(userId, userRole);
    }
}
