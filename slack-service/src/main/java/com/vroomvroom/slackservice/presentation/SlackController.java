package com.vroomvroom.slackservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vroomvroom.slackservice.application.command.SendSlackMessageCommand;
import com.vroomvroom.slackservice.application.dto.SlackMessageReq;
import com.vroomvroom.slackservice.application.service.SlackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {

	private final SlackService slackService;

	@PostMapping("/send")
	public ResponseEntity<String> sendSlackMessage(@RequestBody SendSlackMessageCommand command) {
		slackService.send(command);
		return ResponseEntity.ok("Slack message sent successfully");
	}
}
