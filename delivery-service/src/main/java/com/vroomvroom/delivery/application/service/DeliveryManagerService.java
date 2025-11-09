package com.vroomvroom.delivery.application.service;

import com.vroomvroom.delivery.application.command.CreateManagerCommand;
import com.vroomvroom.delivery.presentation.dto.response.CreateManagerRes;

public interface DeliveryManagerService {

    CreateManagerRes createManager(CreateManagerCommand request);
}
