package com.vroomvroom.slackservice.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.vroomvroom.slackservice.application.command.SendSlackMessageCommand;
import com.vroomvroom.slackservice.exception.SlackErrorCode;
import com.vroomvroom.slackservice.application.dto.SlackMessageReq;
import com.vroomvroom.slackservice.exception.SlackException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService{

	private final WebClient webClient = WebClient.create();

	@Value("${slack.webhook.url:}")
	private String slackWebhookUrl;

	@Override
	public void send(SendSlackMessageCommand command) {
		log.info("[SlackService] Sending message to Slack channel: {}", command.getChannel());

		//  1. Webhook URL 유효성 검사
		if (slackWebhookUrl == null || slackWebhookUrl.isBlank()) {
			throw new SlackException(SlackErrorCode.SLACK_WEBHOOK_MISSING);
		}

		try {
			//  2. Slack 메시지 전송
			Mono<String> response = webClient.post()
											 .uri(slackWebhookUrl)
											 .bodyValue(buildPayload(command))
											 .retrieve()
											 .bodyToMono(String.class);

			response.subscribe(
				success -> log.info("[SlackService] Slack message sent successfully: {}", success),
				error -> {
					log.error("[SlackService] Failed to send Slack message", error);
					throw new SlackException(SlackErrorCode.SLACK_MESSAGE_SEND_FAILED);
				}
			);

		} catch (Exception e) {
			log.error("[SlackService] Unexpected error while sending Slack message", e);
			throw new SlackException(SlackErrorCode.SLACK_MESSAGE_SEND_FAILED);
		}
	}

	private String buildPayload(SendSlackMessageCommand c) {
		//  Slack Webhook JSON 포맷
		return """
        {
          "channel": "%s",
          "username": "%s",
          "text": "%s"
        }
        """.formatted(c.getChannel(), c.getUsername(), c.getText());
	}

}
