package com.vroomvroom.slackservice.application.service;

import com.vroomvroom.slackservice.application.command.SendSlackMessageCommand;
import com.vroomvroom.slackservice.application.dto.SlackMessageReq;

public interface SlackService {
	void send(SendSlackMessageCommand command);
}
